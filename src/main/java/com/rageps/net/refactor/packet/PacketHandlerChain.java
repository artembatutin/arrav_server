package com.rageps.net.refactor.packet;

import com.google.common.base.MoreObjects;
import com.rageps.world.entity.actor.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A chain of {@link PacketHandler}s
 *
 * @param <M> The Message type this chain represents.
 * @author Graham
 * @author Ryley
 */
public final class PacketHandlerChain<M extends Packet> {

	/**
	 * The List of MessageHandlers.
	 */
	private final List<PacketHandler<M>> handlers = new ArrayList<>();

	/**
	 * The Class type of this chain.
	 */
	private final Class<M> type;

	/**
	 * Constructs a new {@link PacketHandlerChain}.
	 *
	 * @param type The Class type of this chain.
	 */
	public PacketHandlerChain(Class<M> type) {
		this.type = type;
	}

	/**
	 * Adds the specified {@link PacketHandler} to this chain.
	 *
	 * @param handler The MessageHandler.
	 */
	public void addHandler(PacketHandler<M> handler) {
		handlers.add(handler);
	}

	/**
	 * Notifies each {@link PacketHandler} in this chain that a {@link Packet} has been received.
	 *
	 * @param player The Player to handle this message for.
	 * @param message The Message.
	 * @return {@code true} iff the Message propagated down the chain without being terminated.
	 */
	public boolean notify(Player player, M message) {
		for (PacketHandler<M> handler : handlers) {
			handler.handle(player, message);

			if (message.terminated()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", type).add("handlers", handlers).toString();
	}

}