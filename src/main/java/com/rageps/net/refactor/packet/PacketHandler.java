package com.rageps.net.refactor.packet;


import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

/**
 * Listens for {@link Packet}s received from the client.
 *
 * @author Graham
 * @author Ryley
 * @param <M> The type of Message this class is listening for.
 */
public abstract class PacketHandler<M extends Packet> {

	/**
	 * The World the Message occurred in.
	 */
	protected final World world;

	/**
	 * Creates the MessageListener.
	 *
	 * @param world The {@link World} the {@link Packet} occurred in.
	 */
	public PacketHandler(World world) {
		this.world = world;
	}

	/**
	 * Handles the Message that was received.
	 *
	 * @param player The player to handle the Message for.
	 * @param message The Message.
	 */
	public abstract void handle(Player player, M message);

}