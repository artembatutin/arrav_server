package com.rageps.net.refactor.release;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.message.Message;

/**
 * A {@link MessageDecoder} decodes a {@link GamePacket} into a {@link Message} object which can be processed by the
 * server.
 *
 * @author Graham
 * @param <M> The type of message.
 */
public abstract class MessageDecoder<M extends Message> {

	/**
	 * Decodes the specified packet into a message.
	 *
	 * @param packet The packet.
	 * @return The message.
	 */
	public abstract M decode(GamePacket packet);

}