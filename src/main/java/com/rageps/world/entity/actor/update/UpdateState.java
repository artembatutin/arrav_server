package com.rageps.world.entity.actor.update;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;

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
