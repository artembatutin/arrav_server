package net.arrav.net.packet.in;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;

/**
 * The message sent from the client when an {@link Player} enters an idle state.
 * @author lare96 <http://github.com/lare96>
 */
public final class IdleStatePacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
	
	}
}
