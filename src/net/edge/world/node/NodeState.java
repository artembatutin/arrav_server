package net.edge.world.node;

import net.edge.world.node.entity.player.Player;

/**
 * An enumeration that defines the states of an {@link Node}.
 */
public enum NodeState {
	/**
	 * An {@link Node} has just been instantiated and is awaiting registration.
	 */
	IDLE,
	
	/**
	 * An {@link Node} has just been registered.
	 */
	ACTIVE,
	
	/**
	 * An {@link Node} has just been unregistered.
	 */
	INACTIVE,
	
	/**
	 * A specific {@link Player} removal state.
	 */
	AWAITING_REMOVAL;
	
}
