package net.edge.content.newcombat.events;

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
        if (event.delay == 0) {
            event.execute();
        } else {
            events.add(event);
        }
    }
    
    public void cancelAll() {
        events.clear();
    }

}
