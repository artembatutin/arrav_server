package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;

/**
 * The message sent from the client when a player uses an item on another item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemNodePacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		final int itemX = buf.getShort(true, ByteOrder.LITTLE);
		final int itemY = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		final int itemId = buf.getShort(ByteTransform.A);
	}
}
