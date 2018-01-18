package net.edge.net.packet;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 6-7-2017.
 */
public interface OutgoingPacket {
	
	default boolean onSent(Player player) {
		return true;
	}
	
	default int size() {
		return 256;
	}
	
	default int opcode() {
		return -1;
	}
	
	ByteBuf write(Player player, GameBuffer msg);
	
}
