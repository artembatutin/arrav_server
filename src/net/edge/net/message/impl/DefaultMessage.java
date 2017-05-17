package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.model.node.entity.player.Player;

/**
 * The decoder used to handle useless messages sent from the client.
 * @author lare96 <http://github.com/lare96>
 */
public final class DefaultMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		
	}
}
