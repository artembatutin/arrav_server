package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;

/**
 * This message sent from the client when the focus of the player's window changes.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class FocusChangeMessage implements PacketReader {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		boolean focused = payload.get(false) == 1;
		player.setFocused(focused);
	}
	
}
