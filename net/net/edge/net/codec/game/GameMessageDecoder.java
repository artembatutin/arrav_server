package net.edge.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.edge.net.NetworkConstants;
import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.IsaacCipher;
import net.edge.net.codec.MessageType;
import net.edge.net.message.GameMessage;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;

/**
 * A {@link ByteToMessageDecoder} implementation that decodes all {@link ByteBuf}s into {@link GameMessage}s.
 * @author lare96 <http://github.org/lare96>
 */
public final class GameMessageDecoder extends ByteToMessageDecoder {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(GameMessageDecoder.class.getName());
	
	/**
	 * The ISAAC that will decrypt incoming messages.
	 */
	private final IsaacCipher decryptor;
	
	/**
	 * The state of the message currently being decoded.
	 */
	private State state = State.OPCODE;
	
	/**
	 * The opcode of the message currently being decoded.
	 */
	private int opcode = -1;
	
	/**
	 * The size of the message currently being decoded.
	 */
	private int size = -1;
	
	/**
	 * The type of the message currently being decoded.
	 */
	private MessageType type = MessageType.RAW;
	
	/**
	 * The message that was decoded and needs to be queued.
	 */
	private Optional<GameMessage> currentMessage = Optional.empty();
	
	/**
	 * Creates a new {@link GameMessageDecoder}.
	 * @param decryptor The decryptor for this decoder.
	 */
	public GameMessageDecoder(IsaacCipher decryptor) {
		this.decryptor = decryptor;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch(state) {
			case OPCODE:
				opcode(in);
				break;
			case SIZE:
				size(in);
				break;
			case PAYLOAD:
				payload(in);
				break;
		}
		currentMessage.ifPresent(m -> {
			out.add(m);
			currentMessage = Optional.empty();
		});
	}
	
	/**
	 * Decodes the opcode of the {@link GameMessage}.
	 * @param in The data being decoded.
	 */
	private void opcode(ByteBuf in) {
		if(in.isReadable()) {
			opcode = in.readUnsignedByte();
			opcode = (opcode - decryptor.nextInt()) & 0xFF;
			size = NetworkConstants.MESSAGE_SIZES[opcode];
			
			if(size == -1) {
				type = MessageType.VARIABLE;
			} else if(size == -2) {
				type = MessageType.VARIABLE_SHORT;
			} else {
				type = MessageType.FIXED;
			}
			
			if(size == 0) {
				queueMessage(Unpooled.EMPTY_BUFFER);
				return;
			}
			state = size == -1 || size == -2 ? State.SIZE : State.PAYLOAD;
		}
	}
	
	/**
	 * Decodes the size of the {@link GameMessage}.
	 * @param in The data being decoded.
	 */
	private void size(ByteBuf in) {
		int bytes = size == -1 ? Byte.BYTES : Short.BYTES;
		
		if(in.isReadable(bytes)) {
			size = 0;
			for(int i = 0; i < bytes; i++) {
				size |= in.readUnsignedByte() << 8 * (bytes - 1 - i);
			}
			state = State.PAYLOAD;
		}
	}
	
	/**
	 * Decodes the payload of the {@link GameMessage}.
	 * @param in The data being decoded.
	 */
	private void payload(ByteBuf in) {
		if(in.isReadable(size)) {
			queueMessage(in.readBytes(size));
		}
	}
	
	/**
	 * Prepares a {@link GameMessage} to be queued upstream and handled on the main game thread.
	 * @param payload The payload of the {@code GameMessage}.
	 */
	private void queueMessage(ByteBuf payload) {
		checkState(opcode >= 0, "opcode < 0");
		checkState(size >= 0, "size < 0");
		checkState(type != MessageType.RAW, "type == MessageType.RAW");
		checkState(!currentMessage.isPresent(), "message already in queue");
		
		try {
			if(NetworkConstants.MESSAGES[opcode] == null) {
				LOGGER.info("Unhandled packet " + opcode + " - " + size);
				currentMessage = null;
				return;
			}
			currentMessage = Optional.of(new GameMessage(opcode, type, ByteMessage.wrap(payload)));
		} finally {
			resetState();
		}
	}
	
	/**
	 * Resets the state of this {@code MessageDecoder} to its default.
	 */
	private void resetState() {
		opcode = -1;
		size = -1;
		state = State.OPCODE;
	}
	
	/**
	 * An enumerated type whose elements represent all of the possible states of this {@code MessageDecoder}.
	 */
	private enum State {
		OPCODE,
		SIZE,
		PAYLOAD
	}
}