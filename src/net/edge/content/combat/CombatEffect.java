package net.edge.content.combat;

import net.edge.content.combat.hit.Hit;
import net.edge.world.entity.actor.Actor;

public interface CombatEffect {

    default boolean canEffect(Actor attacker, Actor defender, Hit hit) {
        return true;
    }

    void execute(Actor attacker, Actor defender, Hit hit);

}
