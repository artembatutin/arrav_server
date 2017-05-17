package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.model.node.entity.player.Player;

/**
 * This message sent from the client when the focus of the player's window changes.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class FocusChangeMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		boolean focused = payload.get(false) == 1;
		player.setFocused(focused);
	}
	
}
