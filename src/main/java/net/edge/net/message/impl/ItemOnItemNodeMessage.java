package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.world.node.entity.player.Player;

/**
 * The message sent from the client when a player uses an item on another item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemNodeMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		final int itemX = payload.getShort(true, ByteOrder.LITTLE);
		final int itemY = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		final int itemId = payload.getShort(ByteTransform.A);
	}
}
