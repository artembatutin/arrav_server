package net.edge.world.entity.actor.combat.attack.listener;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.formula.FormulaModifier;
import net.edge.world.entity.actor.combat.hit.Hit;

/**
 * A combat attack is used to describe what the attacking and defending mobs
 * should do in each stage of combat.
 *
 * @author Michael | Chex
 */
public interface CombatListener<T extends Actor> extends FormulaModifier<T> {

    boolean withinDistance(T attacker, Actor defender);

    /**
     * Checks if the attacker can attack the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    boolean canAttack(T attacker, Actor defender);

    /**
     * Checks if the attacker can attack the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    boolean canOtherAttack(Actor attacker, T defender);

    /**
     * Called when the strategy initializes.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    default void init(T attacker, Actor defender) {}

    /**
     * Called when the strategy starts.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    void start(T attacker, Actor defender, Hit[] hits);

    /**
     * Called when the attacking hit executes on the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit to apply
     */
    void attack(T attacker, Actor defender, Hit hit);

    /**
     * Called when the attacking mob performs an attack on the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit to apply
     */
    void hit(T attacker, Actor defender, Hit hit);

    /**
     * Called when the defending mob blocks a hit from the attacker.
     *
     * @param attacker   the attacking mob
     * @param defender   the defending mob
     * @param hit        the hit being applied
     * @param combatType the combat type for this hit
     */
    void block(Actor attacker, T defender, Hit hit, CombatType combatType);

    /**
     * Called right before the defending mob dies.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void preDeath(Actor attacker, T defender, Hit hit);

    /**
     * Called when the defending mob dies.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void onDeath(Actor attacker, T defender, Hit hit);

    /**
     * Called before attacker killed defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void preKill(T attacker, Actor defender, Hit hit);


    /**
     * Called when attacker killed defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void onKill(T attacker, Actor defender, Hit hit);

    /**
     * Called when attacker does the hitsplat
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void hitsplat(T attacker, Actor defender, Hit hit);

    /**
     * Called when the defending mob finishes their strategy's attack.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    void finishOutgoing(T attacker, Actor defender);

    /**
     * Called when the attacking mob finishes their strategy's attack.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    void finishIncoming(Actor attacker, T defender);


    /**
     * Condition if the strategist hits back.
     * @return hits back on an incoming attack.
     */
    default boolean hitBack() {
        return false;
    }

}

