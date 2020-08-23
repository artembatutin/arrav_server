package com.rageps.net.refactor.packet.out;

import com.rageps.net.packet.OutgoingPacket;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

/**
 * A {@link PacketEncoder} encodes {@link Packet} objects into {@link GamePacket}s which can be sent over the network.
 *
 * @author Graham
 * @param <M> The type of message.
 */
public abstract interface PacketEncoder<M extends Packet> {

	default boolean onSent(M message) {
		return true;
	}

	default GamePacket coordinatePacket(M message, Position lastRegion) {
		return null;
	}

	/**
	 * Encodes the specified message into a packet.
	 *
	 * @param message The message.
	 * @return The packet.
	 */
	GamePacket encode(M message);



}