package net.edge.content.newcombat.events;

import net.edge.content.newcombat.hit.CombatHit;
import net.edge.world.entity.actor.Actor;

public class CombatEvent {
    final int delay;
    private int ticks;

    private Actor defender;
    private CombatHit hit;
    private CombatHit[] hits;
    private EventInterface execute;

    public CombatEvent(Actor defender, int delay, CombatHit hit, CombatHit[] hits, EventInterface execute) {
        this.delay = delay;
        this.defender = defender;
        this.hit = hit;
        this.hits = hits;
        this.execute = execute;
    }

    public CombatEvent(Actor defender, int delay, CombatHit[] hits, EventInterface execute) {
        this.delay = delay;
        this.defender = defender;
        this.hits = hits;
        this.execute = execute;
    }

    boolean canExecute() {
        return ++ticks > delay;
    }

    public void execute() {
        execute.execute(defender, hit, hits);
    }

}
