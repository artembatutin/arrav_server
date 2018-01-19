package net.arrav.world.entity.actor.player.assets.activity;

/**
 * Represents a single hook for a disabled activity.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
@FunctionalInterface
public interface ActivityHook {

	/**
	 * Attempts to execute the specified function
	 */
	void attempt();

}
