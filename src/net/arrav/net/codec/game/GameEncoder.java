package net.arrav.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.arrav.net.codec.crypto.IsaacRandom;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

/**
 * A {@link MessageToByteEncoder} implementation to encode game packets.
 * @author Artem Batutin
 */
public class GameEncoder extends MessageToByteEncoder<OutgoingPacket> {
	
	/**
	 * The player instance.
	 */
	private final Player player;
	
	/**
	 * The message encryptor.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * Creates a new {@link GameEncoder}.
	 */
	public GameEncoder(IsaacRandom encryptor, Player player) {
		this.encryptor = encryptor;
		this.player = player;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, OutgoingPacket out, ByteBuf buf) {
		try {
			ByteBuf outBuf = ctx.channel().alloc().buffer(out.size()).setEncryptor(encryptor);
			outBuf = out.write(this.player, outBuf);
			System.out.println("encoding packet: " + out.getClass() + " size: " + outBuf.readableBytes());
			buf.writeBytes(outBuf);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
