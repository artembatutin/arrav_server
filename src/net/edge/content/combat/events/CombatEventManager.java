package net.edge.content.combat.events;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CombatEventManager {
    private List<CombatEvent> events = new LinkedList<>();

    public void sequence() {
        for (Iterator<CombatEvent> iterator = events.iterator(); iterator.hasNext();) {
            CombatEvent event = iterator.next();
            if (event.canExecute()) {
                event.execute();
                iterator.remove();
            }
        }
    }
    
    public void add(CombatEvent event) {
        events.add(event);
    }

    public void cancelAll() {
        events.clear();
    }

}
