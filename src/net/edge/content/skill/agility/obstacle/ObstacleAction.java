package net.edge.content.skill.agility.obstacle;

import net.edge.world.entity.actor.player.Player;

/**
 * The Obstacle Policy each obstacle should implement and bond to.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public interface ObstacleAction {
	
	/**
	 * The obstacle functionality handled upon interacting with the object.
	 * @return the obstacle action which belongs to this obstacle.
	 */
	ObstacleActivity activity(Player player);
}
