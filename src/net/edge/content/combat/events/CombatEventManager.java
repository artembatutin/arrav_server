package net.edge.content.combat.events;

import net.edge.world.entity.actor.Actor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CombatEventManager<T extends Actor> {
    private List<CombatEvent<T>> events = new CopyOnWriteArrayList<>();

    public void sequence() {
        for (CombatEvent<T> event : events) {
            event.tick();


            while (event.canExecute()) {
                event.execute();
            }

            if (!event.isActive()) {
                events.remove(event);
            }
        }
    }
    
    public void add(CombatEvent<T> event) {
        events.add(event);
    }

}
