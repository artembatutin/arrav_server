package net.edge.content.combat.events;

import net.edge.content.combat.hit.CombatHit;
import net.edge.world.entity.actor.Actor;

public interface EventInterface{

    void execute(Actor defender, CombatHit hit);

}
