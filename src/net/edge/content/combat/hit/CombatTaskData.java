package net.edge.content.combat.hit;

import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

/**
 * Holds variables to generate a combat task for an actor.
 *
 * @author Michael | Chex
 */
public final class CombatTaskData<T extends Actor> {

    /** The attacker. */
    private final T attacker;

    /** The defender. */
    private final Actor defender;

    /** The combat hit. */
    private final CombatHit hit;

    /** The combat strategy. */
    private final CombatStrategy<? super T> strategy;

    /** The active state. */
    private boolean active;

    /**
     * Constructs a new {@code CombatHitData} object.
     *
     * @param attacker the attacker
     * @param defender the defender
     * @param hit      the combat hit
     * @param strategy the strategy
     */
    public CombatTaskData(T attacker, Actor defender, CombatHit hit, CombatStrategy<? super T> strategy) {
        this.attacker = attacker;
        this.defender = defender;
        this.hit = hit;
        this.strategy = strategy;
    }

    /**
     * Gets the attacker.
     *
     * @return the attacker
     */
    public T getAttacker() {
        return attacker;
    }

    /**
     * Gets the defender.
     *
     * @return the defender
     */
    public Actor getDefender() {
        return defender;
    }

    /**
     * Gets the hit.
     *
     * @return the hit
     */
    public CombatHit getHit() {
        return hit;
    }

    /**
     * Gets the hit delay.
     *
     * @return the hit delay.
     */
    public int getHitDelay() {
        return hit.getHitDelay();
    }

    /**
     * Gets the hitsplat delay.
     *
     * @return the hitsplat delay
     */
    public int getHitsplatDelay() {
        return hit.getHitsplatDelay();
    }

    /**
     * Gets the combat strategy.
     *
     * @return the combat strategy
     */
    public CombatStrategy<? super T> getStrategy() {
        return strategy;
    }

    /**
     * Checks if this hit data is still active.
     *
     * @return {@code true} if this hit data is still active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active flag.
     *
     * @param active if this hit data is still active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
