package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 26-6-2017.
 */
public final class RevenantCombatStrategy implements CombatStrategy {

    /**
     * Determines if {@code character} is able to make an attack on
     * {@code victim}.5
     *
     * @param character the character to has if able.
     * @param victim    the character being attacked.
     * @return {@code true} if an attack can be made, {@code false} otherwise.
     */
    @Override
    public boolean canOutgoingAttack(Actor character, Actor victim) {
        return true;
    }

    /**
     * Executed when {@code character} has passed the initial {@code canAttack}
     * check and is about to attack {@code victim}.
     *
     * @param character the character that is attacking.
     * @param victim    the character being attacked.
     * @return a container holding the data for the attack.
     */
    @Override
    public CombatHit outgoingAttack(Actor character, Actor victim) {
        return null;
    }

    /**
     * Determines the delay for when {@code character} will attack.
     *
     * @param character the character waiting to attack.
     * @return the value that the attack timer should be reset to.
     */
    @Override
    public int attackDelay(Actor character) {
        return 0;
    }

    /**
     * Determines how close {@code character} must be to attack.
     *
     * @param character the character that is attacking.
     * @return the radius that the character must be in to attack.
     */
    @Override
    public int attackDistance(Actor character) {
        return 0;
    }

    /**
     * The NPCs that will be assigned this combat strategy.
     *
     * @return the array of assigned NPCs.
     */
    @Override
    public int[] getNpcs() {
        return new int[0];
    }
}
