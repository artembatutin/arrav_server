package net.arrav.net.packet;

import io.netty.buffer.ByteBuf;
import net.arrav.world.entity.actor.player.Player;

/**
 * Represents a packet reader which returns a message.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@FunctionalInterface
public interface IncomingPacket {
	
	/**
	 * Handles the message designated to {@code opcode}.
	 * @param player the player this message is being handled for.
	 * @param opcode the opcode of this message.
	 * @param size the size of this message.
	 * @param buf the data contained within this message.
	 */
	void handle(Player player, int opcode, int size, ByteBuf buf);
}
