package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.node.entity.player.Player;

/**
 * The message sent from the client when the player selects a new speciality.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SpecialitySelectionMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		payload.get();
	}
}
