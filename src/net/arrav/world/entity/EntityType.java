package net.arrav.world.entity;

import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;
import net.arrav.world.entity.region.Region;
import net.arrav.world.object.GameObject;

/**
 * The enumerated type whose elements represent the different types of {@link Entity} implementations.
 * @author lare96 <http://github.com/lare96>
 */
public enum EntityType {
	
	/**
	 * The element used to represent the {@link GroundItem} implementation.
	 */
	ITEM,
	
	/**
	 * The element used to represent the {@link GameObject} implementation.
	 */
	OBJECT,
	
	/**
	 * The element used to represent the {@link Player} implementation.
	 */
	PLAYER,
	
	/**
	 * The element used to represent the {@link Mob} implementation.
	 */
	NPC,
	
	/**
	 * The element used to represent the {@link Region} implementation.
	 */
	REGION
}