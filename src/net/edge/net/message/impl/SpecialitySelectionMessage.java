package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.world.model.node.entity.player.Player;
import net.edge.net.message.InputMessageListener;

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
