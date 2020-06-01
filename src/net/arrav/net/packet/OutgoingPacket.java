package net.arrav.net.packet;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 6-7-2017.
 */
public interface OutgoingPacket {
	
	default boolean onSent(Player player) {
		return true;
	}
	
	default OutgoingPacket coordinatePacket() {
		return null;
	}
	
	default int size() {
		return 128;
	}
	
	default int opcode() {
		return -1;
	}
	
	GamePacket write(Player player, ByteBuf buf);
	
}
