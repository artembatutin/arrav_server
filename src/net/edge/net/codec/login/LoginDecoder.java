package net.edge.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.edge.GameConstants;
import net.edge.net.NetworkConstants;
import net.edge.net.codec.crypto.IsaacRandom;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;
import net.edge.net.packet.PacketUtils;
import net.edge.util.TextUtils;
import net.edge.world.World;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

/**
 * A {@link ByteToMessageDecoder} implementation that decodes the entire login protocol in states.
 * @author lare96 <http://github.org/lare96>
 */
public final class LoginDecoder extends ByteToMessageDecoder {

	/**
	 * A cryptographically secure random number generator.
	 */
	private static final Random RANDOM = new SecureRandom();

	/**
	 * The current state of decoding the protocol.
	 */
	private State state = State.HANDSHAKE;

	/**
	 * The size of the last portion of the protocol.
	 */
	private int rsaBlockSize;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch(state) {
			case HANDSHAKE:
				decodeHandshake(ctx, in, out);
				state = State.LOGIN_TYPE;
				break;
			case LOGIN_TYPE:
				decodeLoginType(ctx, in, out);
				state = State.RSA_BLOCK;
				break;
			case RSA_BLOCK:
				decodeRsaBlock(ctx, in, out);
				break;
		}
	}

	/**
	 * Decodes the handshake portion of the login protocol.
	 * @param ctx The channel handler context.
	 * @param in  The data that is being decoded.
	 * @param out The list of decoded messages.
	 * @throws Exception If any exceptions occur while decoding this portion of the protocol.
	 */
	private void decodeHandshake(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() >= 14) {
			int build = in.readUnsignedShort();
			if(build != GameConstants.CLIENT_BUILD) {
				write(ctx, LoginCode.WRONG_BUILD_NUMBER);
				return;
			}
			//mac address
			int macId = in.readInt();
			String mac = String.valueOf(macId);
			ctx.channel().attr(NetworkConstants.USR_MAC).set(mac);
			if(HostManager.contains(mac, HostListType.BANNED_MAC)) {
				write(ctx, LoginCode.ACCOUNT_DISABLED);
				return;
			}
			//username hash
			long usernameHash = in.readLong();
			ctx.channel().attr(NetworkConstants.USR_HASH).set(usernameHash);
			if (World.get().getPlayer(usernameHash).isPresent()) {
				write(ctx, LoginCode.ACCOUNT_ONLINE);
				return;
			}
			ByteBuf buf = ctx.alloc().buffer(17);
			buf.writeLong(0);
			buf.writeByte(0);
			buf.writeLong(RANDOM.nextLong());
			ctx.writeAndFlush(buf, ctx.voidPromise());
		}
	}

	/**
	 * Decodes the portion of the login protocol where the RSA block size and mac address are determined.
	 * @param ctx The channel handler context.
	 * @param in  The data that is being decoded.
	 * @param out The list of decoded messages.
	 * @throws Exception If any exceptions occur while decoding this portion of the protocol.
	 */
	private void decodeLoginType(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() >= 1) {
			//RSA size
			rsaBlockSize = in.readUnsignedByte();
			if(rsaBlockSize == 0) {
				write(ctx, LoginCode.COULD_NOT_COMPLETE_LOGIN);
			}
		}
	}

	/**
	 * Decodes the RSA portion of the login protocol.
	 * @param ctx The channel handler context.
	 * @param in  The data that is being decoded.
	 * @param out The list of decoded messages.
	 */
	private void decodeRsaBlock(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		if(in.readableBytes() >= rsaBlockSize) {
			int expectedSize = in.readUnsignedByte();
			if(expectedSize != rsaBlockSize - 1) {
				write(ctx, LoginCode.COULD_NOT_COMPLETE_LOGIN);
				return;
			}
			byte[] rsaBytes = new byte[rsaBlockSize - 1];
			in.readBytes(rsaBytes);
			byte[] rsaData = new BigInteger(rsaBytes).toByteArray();
			ByteBuf rsaBuffer = Unpooled.wrappedBuffer(rsaData);
			try {
				long clientHalf = rsaBuffer.readLong();
				long serverHalf = rsaBuffer.readLong();
				int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf};
				IsaacRandom decryptor = new IsaacRandom(isaacSeed);
				for (int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				IsaacRandom encryptor = new IsaacRandom(isaacSeed);
				String password = PacketUtils.getCString(rsaBuffer).toLowerCase();
				long usernameHash = ctx.channel().attr(NetworkConstants.USR_HASH).get();
				String username = TextUtils.hashToName(usernameHash).toLowerCase().trim();
				String macAddress = ctx.channel().attr(NetworkConstants.USR_MAC).get();
				out.add(new LoginRequest(usernameHash, username, password, macAddress, encryptor, decryptor));
			} finally {
				if (rsaBuffer.isReadable()) {
					rsaBuffer.release();
				}
			}
		}
	}

	/**
	 * Wrties a closed response to the login channel.
	 */
	private void write(ChannelHandlerContext ctx, LoginCode response) {
		System.out.println("written: " + response);
		Channel channel = ctx.channel();
		LoginResponse message = new LoginResponse(response);
		ByteBuf initialMessage = ctx.alloc().buffer(8).writeLong(0); // Write initial message.
		channel.write(initialMessage, channel.voidPromise());
		channel.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE); // Write response message.
	}

	/**
	 * An enumerated type whose elements represent the various stages of the login protocol.
	 */
	private enum State {
		HANDSHAKE,
		LOGIN_TYPE,
		RSA_BLOCK
	}
}