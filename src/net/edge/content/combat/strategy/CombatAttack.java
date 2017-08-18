package net.edge.content.combat.strategy;

import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.hit.Hit;
import net.edge.world.entity.actor.Actor;

import java.util.Optional;

/**
 * A combat attack is used to describe what the attacking and defending actors
 * should do in each stage of combat.
 *
 * @author Michael | Chex
 */
public interface CombatAttack<T extends Actor> {

    /**
     * Called when the attacking hit executes on the defender.
     *  @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit to apply
     */
    void attack(T attacker, Actor defender, Hit hit);

    /**
     * Called when the attacking actor performs an attack on the defender.
     *  @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit to apply
     */
    void hit(T attacker, Actor defender, Hit hit);

    /**
     * Called when the hitsplat is applied to the defender.
     *  @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit to apply
     */
    void hitsplat(T attacker, Actor defender, Hit hit);

    /**
     * Called when the defending actor blocks a hit from the attacker.
     *  @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit being applied
     */
    void block(Actor attacker, T defender, Hit hit);

    /**
     * Called when the defending actor blocks a hit from the attacker.
     *  @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit that killed the defender
     */
    void onDeath(Actor attacker, T defender, Hit hit);

    /**
     * Called when the defending actor blocks a hit from the attacker.
     *  @param attacker the attacking actor
     * @param defender the defending actor
     */
    void finish(T attacker, Actor defender);

    /**
     * Gets the attack modifier of this combat attack, wrapped around an {@link
     * Optional} for ease of use.
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @return an {@link Optional} wrapping the {@link AttackModifier} if
     * present.
     */
    default Optional<AttackModifier> getModifier(T attacker, Actor defender) {
        return Optional.empty();
    }
}

