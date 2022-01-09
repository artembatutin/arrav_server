package com.rageps.net.refactor.packet;


import com.rageps.world.entity.actor.player.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A group of {@link PacketHandlerChain}s classified by the {@link Packet} type.
 *
 * @author Graham
 * @author Ryley
 * @author Major
 */
public final class PacketHandlerChainSet {

	/**
	 * The {@link Map} of {@link Packet} {@link Class} types to {@link PacketHandlerChain}s
	 */
	private final Map<Class<? extends Packet>, PacketHandlerChain<? extends Packet>> chains = new HashMap<>();

	/**
	 * The {@link Map} of {@link Packet} {@link Class} types to {@link Deque}s of all Classes in the chain.
	 */
	private final Map<Class<? extends Packet>, Deque<Class<? extends Packet>>> classes = new HashMap<>();

	/**
	 * Notifies the appropriate {@link PacketHandlerChain} that a {@link Packet} has been received.
	 *
	 * @param player The {@link Player} receiving the Message.
	 * @param message The Message.
	 * @return {@code true} iff the Message propagated down the chain without being terminated.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Packet> boolean notify(Player player, M message) {
		Deque<Class<? extends Packet>> classes = this.classes.computeIfAbsent(message.getClass(),
				this::getMessageClasses);

		for (Class<? extends Packet> type : classes) {
			PacketHandlerChain<? super M> chain = (PacketHandlerChain<? super M>) chains.get(type);

			if (chain != null && !chain.notify(player, message)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Places the {@link PacketHandlerChain} into this set.
	 *
	 * @param clazz The {@link Class} to associate the MessageHandlerChain with.
	 * @param handler The MessageHandlerChain.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Packet> void putHandler(Class<M> clazz, PacketHandler<? extends Packet> handler) {
		PacketHandlerChain<M> chain = (PacketHandlerChain<M>) chains.computeIfAbsent(clazz, PacketHandlerChain::new);
		chain.addHandler((PacketHandler<M>) handler);
	}

	/**
	 * Gets the {@link Deque} of {@link Class}es that can be handled.
	 *
	 * @param type The Class type of the message. Must not be the Class for {@link Packet} itself.
	 * @return The Deque of Classes. Will never be {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private <M extends Packet> Deque<Class<? extends Packet>> getMessageClasses(Class<M> type) {
		Deque<Class<? extends Packet>> classes = new ArrayDeque<>();
		Class<? super M> clazz = type;

		do {
			classes.addFirst((Class<? extends Packet>) clazz);
			clazz = clazz.getSuperclass();
		} while (clazz != Packet.class);

		return classes;
	}

}