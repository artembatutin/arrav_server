package net.arrav.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.arrav.net.NetworkConstants;
import net.arrav.net.codec.crypto.IsaacRandom;
import net.arrav.util.StatefulFrameDecoder;

import java.util.List;
import java.util.logging.Logger;

/**
 * A {@link StatefulFrameDecoder} implementation of all game fragments.
 * @author Artem Batutin
 */
public class GameDecoder extends StatefulFrameDecoder<GameState> {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(GameDecoder.class.getName());
	
	/**
	 * The state of the message currently being decoded.
	 */
	private GameState gameState = GameState.OPCODE;
	
	/**
	 * The type of the message currently being decoded.
	 */
	private GamePacketType type = GamePacketType.RAW;
	
	/**
	 * The opcode of the message currently being decoded.
	 */
	private int opcode = -1;
	
	/**
	 * The size of the message currently being decoded.
	 */
	private int size = -1;
	
	/**
	 * The message decryptor.
	 */
	private final IsaacRandom decryptor;
	
	/**
	 * Creates the stateful frame decoder with the specified initial state.
	 * @throws NullPointerException If the state is {@code null}.
	 */
	public GameDecoder(IsaacRandom decryptor) {
		super(GameState.OPCODE);
		this.decryptor = decryptor;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, GameState state) throws Exception {
		try {
			switch(state) {
				case OPCODE:
					opcode(in, out);
					break;
				case SIZE:
					size(in, out);
					break;
				case PAYLOAD:
					payload(in, out);
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Decodes the opcode of the {@link ByteBuf}.
	 */
	private void opcode(ByteBuf in, List<Object> out) {
		if(in.isReadable()) {
			opcode = in.readUnsignedByte();
			opcode = (opcode - decryptor.nextInt()) & 0xFF;
			size = NetworkConstants.MESSAGE_SIZES[opcode];
			if(size == -1) {
				type = GamePacketType.VARIABLE_BYTE;
			} else if(size == -2) {
				type = GamePacketType.VARIABLE_SHORT;
			} else {
				type = GamePacketType.FIXED;
			}
			
			if(opcode == 0) {
				opcode(in, out);
				return;
			}
			
			if(size == 0) {
				if(NetworkConstants.MESSAGES[opcode] != null)
					queueMessage(Unpooled.EMPTY_BUFFER, out);
				opcode(in, out);
				return;
			}
			
			gameState = size == -1 || size == -2 ? GameState.SIZE : GameState.PAYLOAD;
			if(gameState == GameState.SIZE) {
				size(in, out);
			} else {
				payload(in, out);
			}
		}
	}
	
	/**
	 * Decodes the size of the {@link ByteBuf}.
	 */
	private void size(ByteBuf in, List<Object> out) {
		int bytes = size == -1 ? Byte.BYTES : Short.BYTES;
		if(in.isReadable(bytes)) {
			size = 0;
			for(int i = 0; i < bytes; i++) {
				size |= in.readUnsignedByte() << 8 * (bytes - 1 - i);
			}
			gameState = GameState.PAYLOAD;
			payload(in, out);
		}
	}
	
	/**
	 * Decodes the payload of the {@link ByteBuf}.
	 */
	private void payload(ByteBuf in, List<Object> out) {
		if(in.isReadable(size)) {
			if(NetworkConstants.MESSAGES[opcode] == null) {
				in.skipBytes(size);//skip bytes.
				LOGGER.info("Skipped unhandled packet " + opcode + " - " + size);
				resetMessage();
				opcode(in, out);
			} else {
				ByteBuf newBuffer = in.readBytes(size);
				queueMessage(newBuffer, out);
				opcode(in, out);
			}
		}
	}
	
	/**
	 * Prepares a {@link ByteBuf} to be queued upstream and handled on the main game thread.
	 * @param payload The payload of the {@code Packet}.
	 */
	private void queueMessage(ByteBuf payload, List<Object> out) {
		try {
			GamePacket packet = new GamePacket(opcode, payload, type);
			if(packet.getOpcode() != 0) {
				//todo: out message
				//if(packet.getOpcode() == 41) {
				//handle(packet);//item equipping
				//return;
				//}
				out.add(packet);
			}
		} finally {
			resetMessage();
		}
	}
	
	/**
	 * Resets the incoming message procedure.
	 */
	private void resetMessage() {
		opcode = -1;
		size = -1;
		gameState = GameState.OPCODE;
	}
}
