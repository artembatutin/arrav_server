package net.edge.world.entity.actor.combat.hit;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.strategy.CombatStrategy;

/**
 * Holds variables to generate a combat task for an actor.
 * @author Michael | Chex
 */
public final class CombatTaskData<T extends Actor> {
	
	/**
	 * The attacker.
	 */
	private final T attacker;
	
	/**
	 * The defender.
	 */
	private final Actor defender;
	
	/**
	 * The combat hit.
	 */
	private final CombatHit hit;
	
	/**
	 * The combat strategy.
	 */
	private final CombatStrategy<? super T> strategy;
	
	/**
	 * Whether or not this hit is the first one.
	 */
	private final boolean firstHit;
	
	/**
	 * Constructs a new {@code CombatHitData} object.
	 * @param attacker the attacker
	 * @param defender the defender
	 * @param hit      the combat hit
	 * @param strategy the strategy
	 * @param firstHit whether or not this hit is the first one
	 */
	public CombatTaskData(T attacker, Actor defender, CombatHit hit, CombatStrategy<? super T> strategy, boolean firstHit) {
		this.attacker = attacker;
		this.defender = defender;
		this.hit = hit;
		this.strategy = strategy;
		this.firstHit = firstHit;
	}
	
	/**
	 * Gets the attacker.
	 * @return the attacker
	 */
	public T getAttacker() {
		return attacker;
	}
	
	/**
	 * Gets the defender.
	 * @return the defender
	 */
	public Actor getDefender() {
		return defender;
	}
	
	/**
	 * Gets the hit.
	 * @return the hit
	 */
	public CombatHit getHit() {
		return hit;
	}
	
	/**
	 * Gets the hit delay.
	 * @return the hit delay.
	 */
	public int getHitDelay() {
		return hit.getHitDelay();
	}
	
	/**
	 * Gets the hitsplat delay.
	 * @return the hitsplat delay
	 */
	public int getHitsplatDelay() {
		return hit.getHitsplatDelay();
	}
	
	/**
	 * Gets the combat strategy.
	 * @return the combat strategy
	 */
	public CombatStrategy<? super T> getStrategy() {
		return strategy;
	}
	
	/**
	 * Checks if this hit data is the first hit.
	 * @return {@code true} if this hit data is the first hit
	 */
	public boolean isFirstHit() {
		return firstHit;
	}
	
}
