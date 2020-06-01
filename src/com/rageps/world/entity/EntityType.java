package com.rageps.world.entity;

import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.region.Region;

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