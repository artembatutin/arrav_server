package net.edge.world.entity.actor.combat.attack;

import net.edge.world.entity.actor.Actor;

/**
 * Listens for modifiers to the combat formulas.
 *
 * @author Michael | Chex
 */
public interface ModifierListener {

    /**
     * Modifies a variable.
     *
     * @param attacker the attacker
     * @param defender the defender
     * @param variable the variable to modify
     * @return the modified variable
     */
    int modify(Actor attacker, Actor defender, int variable);

    /**
     * Returns a function that always returns its input argument.
     *
     * @return a function that always returns its input argument
     */
    static ModifierListener identity() {
        return (attacker, defender, variable) -> variable;
    }

}
