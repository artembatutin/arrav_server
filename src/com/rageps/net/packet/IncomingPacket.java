package com.rageps.net.packet;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.player.Player;

/**
 * Represents a packet reader which returns a message.
 * @author Artem Batutin
 */
@FunctionalInterface
public interface IncomingPacket {
	
	/**
	 * Handles the message designated to {@code opcode}.
	 * @param player the player this message is being handled for.
	 * @param opcode the opcode of this message.
	 * @param size the size of this message.
	 * @param in the incoming packet.
	 */
	void handle(Player player, int opcode, int size, GamePacket in);
}
