package net.edge.content.newcombat;

import net.edge.content.newcombat.hit.Hit;
import net.edge.world.entity.actor.Actor;

public interface CombatEffect {

    default boolean canEffect(Actor attacker, Actor defender, Hit hit) {
        return true;
    }

    void execute(Actor attacker, Actor defender, Hit hit);

}
