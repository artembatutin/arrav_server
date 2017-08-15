package net.edge.content.newcombat.strategy;

import net.edge.content.newcombat.attack.AttackModifier;
import net.edge.content.newcombat.hit.Hit;
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
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit to apply
     * @param hits     all of the hits in this attack
     */
    void attack(T attacker, Actor defender, Hit hit, Hit[] hits);

    /**
     * Called when the attacking actor performs an attack on the defender.
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit to apply
     * @param hits     all of the hits in this attack
     */
    void hit(T attacker, Actor defender, Hit hit, Hit[] hits);

    /**
     * Called when the hitsplat is applied to the defender.
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit to apply
     * @param hits     all of the hits in this attack
     */
    void hitsplat(T attacker, Actor defender, Hit hit, Hit[] hits);

    /**
     * Called when the defending actor blocks a hit from the attacker.
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit being applied
     */
    void block(Actor attacker, T defender, Hit hit, Hit[] hits);

    /**
     * Called when the defending actor blocks a hit from the attacker.
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @param hit      the hit that killed the defender
     * @param hits     all of the hits in this attack
     */
    void onDeath(Actor attacker, T defender, Hit hit, Hit[] hits);

    /**
     * Called when the defending actor blocks a hit from the attacker.
     *
     * @param attacker the attacking actor
     * @param defender the defending actor
     * @param hits     all of the hits in this attack
     */
    void finish(T attacker, Actor defender, Hit[] hits);

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

