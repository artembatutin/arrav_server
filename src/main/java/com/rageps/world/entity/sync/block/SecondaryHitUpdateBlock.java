package com.rageps.world.entity.sync.block;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;

/**
 * The secondary hit update {@link SynchronizationBlock}. This is used for when multiple attacks happen at once (for
 * example, the dragon-dagger special attack). Both players and npcs can utilise this block.
 *
 * @author Major
 */
public final class SecondaryHitUpdateBlock extends SynchronizationBlock {

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
	 * Creates a new secondary hit update block.
	 *
	 * @param hit The hit being appended.
	 * @param currentHealth The current health of the mob.
	 * @param maximumHealth The maximum health of the mob.
	 */
	SecondaryHitUpdateBlock(Hit hit, int currentHealth, int maximumHealth, Actor source) {
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
	 * @return The hit.
	 */
	public Hit getHit() {
		return hit;
	}
}