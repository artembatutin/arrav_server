package com.rageps.net.refactor.release;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.message.Message;

/**
 * A {@link MessageEncoder} encodes {@link Message} objects into {@link GamePacket}s which can be sent over the network.
 *
 * @author Graham
 * @param <M> The type of message.
 */
public abstract class MessageEncoder<M extends Message> {

	/**
	 * Encodes the specified message into a packet.
	 *
	 * @param message The message.
	 * @return The packet.
	 */
	public abstract GamePacket encode(M message);

}