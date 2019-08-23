package net.arrav.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.arrav.net.codec.crypto.IsaacRandom;
import net.arrav.world.entity.actor.player.Player;

/**
 * A {@link MessageToByteEncoder} implementation to encode game packets.
 * @author Artem Batutin
 */
public class GameEncoder extends MessageToByteEncoder<GamePacket> {
	
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
	protected void encode(ChannelHandlerContext ctx, GamePacket packet, ByteBuf buf) {
		try {
			packet.writePacket(buf, encryptor);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
