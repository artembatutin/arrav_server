package net.edge.content.newcombat.events;

import net.edge.content.newcombat.hit.CombatHit;
import net.edge.world.entity.actor.Actor;

public interface EventInterface{

    void execute(Actor defender, CombatHit hit, CombatHit[] hits);

}
