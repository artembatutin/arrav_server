package com.rageps.world.entity.sync.block;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;

/**
 * The hit update {@link SynchronizationBlock}. Both npcs and players can utilise this block.
 *
 * @author Major
 */
public final class HitUpdateBlock extends SynchronizationBlock {

	/**
	 * The mob's current health.
	 */
	private final int currentHealth;

	/**
	 * The hit.
	 */
	private final Hit hit;

	/**
	 * The mob's maximum health.
	 */
	private final int maximumHealth;

	private final Actor source;



	/**
	 * Creates the hit update block.
	 *
	 * @param hit The hit being appended
	 * @param currentHealth The current health of the mob.
	 * @param maximumHealth The maximum health of the mob.
	 */
	HitUpdateBlock(Hit hit, int currentHealth, int maximumHealth, Actor source) {
		this.hit = hit;
		this.currentHealth = currentHealth;
		this.maximumHealth = maximumHealth;
		this.source = source;
	}

	public Actor getSource() {
		return source;
	}


	/**
	 * Gets the current health of the mob.
	 *
	 * @return The current health;
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}


	/**
	 * Gets the maximum health of the mob.
	 *
	 * @return The maximum health.
	 */
	public int getMaximumHealth() {
		return maximumHealth;
	}

	/**
	 * Gets the hit.
	 *
	 * @return The type.
	 */
	public Hit getHit() {
		return hit;
	}

}