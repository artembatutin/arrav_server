package net.edge.net.packet;

import net.edge.net.codec.ByteMessage;
import net.edge.world.node.entity.player.Player;

/**
 * Represents a packet reader which returns a message.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@FunctionalInterface
public interface PacketReader {
	
	/**
	 * Handles the message designated to {@code opcode}.
	 * @param player  the player this message is being handled for.
	 * @param opcode  the opcode of this message.
	 * @param size    the size of this message.
	 * @param payload the data contained within this message.
	 */
	void handle(Player player, int opcode, int size, ByteMessage payload);
}
