package net.edge.world.entity.actor.update;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * An enumerated type whose elements represent the updating states.
 */
public enum UpdateState {
	
	/**
	 * A {@link Player} is updating for themself, only relevant for {@code Player} updating.
	 */
	UPDATE_SELF,
	
	/**
	 * A {@link Player} is updating for the {@link Actor}s around them.
	 */
	UPDATE_LOCAL,
	
	/**
	 * A {@link Player} is adding new {@link Actor}s that have just appeared around them.
	 */
	ADD_LOCAL
}
