package net.edge.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.crypto.IsaacRandom;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

/**
 * A {@link MessageToByteEncoder} which encodes in-game packets.
 * @author Artem Batutin
 */
public final class GameEncoder extends MessageToByteEncoder<OutgoingPacket> {
	
	/**
	 * The player this encoder is dedicated to.
	 */
	private final Player player;
	
	/**
	 * The ISAAC that will encrypt outgoing messages.
	 */
	private final IsaacRandom encryptor;
	
	public GameEncoder(IsaacRandom encryptor, Player player) {
		this.encryptor = encryptor;
		this.player = player;
	}
	
	@Override
	protected void encode(io.netty.channel.ChannelHandlerContext ctx, OutgoingPacket msg, ByteBuf out) throws Exception {
		msg.write(player, new GameBuffer(out, encryptor));
	}
}