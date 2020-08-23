package com.rageps.net.refactor.packet.in;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.packet.Packet;

/**
 * A {@link PacketDecoder} decodes a {@link GamePacket} into a {@link Packet} object which can be processed by the
 * server.
 *
 * @author Graham
 * @param <M> The type of message.
 */
public interface PacketDecoder<M extends Packet> {

	/**
	 * Decodes the specified packet into a message.
	 *
	 * @param packet The packet.
	 * @return The message.
	 */
	M decode(GamePacket packet);

}