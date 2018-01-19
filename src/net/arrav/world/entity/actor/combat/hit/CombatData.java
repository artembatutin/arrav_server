package net.arrav.world.entity.actor.combat.hit;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.strategy.CombatStrategy;

/**
 * Holds variables to generate combat data of an actor.
 * @author Michael | Chex
 */
public final class CombatData<T extends Actor> {
	
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
	 * Whether or not this hit is the last one.
	 */
	private final boolean lastHit;
	
	/**
	 * Constructs a new {@code CombatHitData} object.
	 * @param attacker the attacker
	 * @param defender the defender
	 * @param hit      the combat hit
	 * @param strategy the strategy
	 * @param lastHit whether or not this hit is the first one
	 */
	public CombatData(T attacker, Actor defender, CombatHit hit, CombatStrategy<? super T> strategy, boolean lastHit) {
		this.attacker = attacker;
		this.defender = defender;
		this.hit = hit;
		this.strategy = strategy;
		this.lastHit = lastHit;
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
	 * Checks if this hit data is the last hit.
	 * @return {@code true} if this hit data is the last hit
	 */
	public boolean isLastHit() {
		return lastHit;
	}
	
}
