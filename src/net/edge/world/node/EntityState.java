package net.edge.world.node;

import net.edge.world.node.actor.player.Player;

/**
 * An enumeration that defines the states of an {@link Entity}.
 */
public enum EntityState {
	/**
	 * An {@link Entity} has just been instantiated and is awaiting registration.
	 */
	IDLE,
	
	/**
	 * An {@link Entity} has just been registered.
	 */
	ACTIVE,
	
	/**
	 * An {@link Entity} has just been unregistered.
	 */
	INACTIVE,
	
	/**
	 * A specific {@link Player} removal state.
	 */
	AWAITING_REMOVAL
	
}
