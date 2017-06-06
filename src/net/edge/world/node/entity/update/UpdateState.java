package net.edge.world.node.entity.update;

import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;

/**
 * An enumerated type whose elements represent the updating states.
 */
public enum UpdateState {
	
	/**
	 * A {@link Player} is updating for themself, only relevant for {@code Player} updating.
	 */
	UPDATE_SELF,
	
	/**
	 * A {@link Player} is updating for the {@link EntityNode}s around them.
	 */
	UPDATE_LOCAL,
	
	/**
	 * A {@link Player} is adding new {@link EntityNode}s that have just appeared around them.
	 */
	ADD_LOCAL
}
