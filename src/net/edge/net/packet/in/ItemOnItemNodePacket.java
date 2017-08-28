package net.edge.net.packet.in;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;

/**
 * The message sent from the client when a player uses an item on another item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemNodePacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		final int itemX = payload.getShort(true, ByteOrder.LITTLE);
		final int itemY = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		final int itemId = payload.getShort(ByteTransform.A);
	}
}
