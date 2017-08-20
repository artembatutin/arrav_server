package net.edge.content.combat.events;

import net.edge.world.entity.actor.Actor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CombatEventManager {
    private List<CombatEvent> events = new CopyOnWriteArrayList<>();

    public void sequence() {
        for (CombatEvent event : events) {
            if (event.canExecute()) {
                event.execute();
                events.remove(event);
            }
        }
    }
    
    public void add(CombatEvent event) {
        events.add(event);
    }

    public void cancel(Actor defender) {
        events.removeIf(next -> next.getDefender() == defender);
    }

}
