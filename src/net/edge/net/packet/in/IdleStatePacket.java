package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;

/**
 * The message sent from the client when an {@link Player} enters an idle state.
 * @author lare96 <http://github.com/lare96>
 */
public final class IdleStatePacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
	
	}
}
