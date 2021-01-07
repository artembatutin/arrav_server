package com.rageps.net.refactor.packet.out;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;

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

	default CoordinatesPacket coordinatePacket(M message) {
		return null;
	}

	/**
	 * Encodes the specified message into a packet.
	 *
	 * @param message The message.
	 * @return The packet.
	 */
	public abstract GamePacket encode(M message);



}