package net.edge.content.combat.events;

import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.world.entity.actor.Actor;

import java.util.List;

public abstract class CombatEvent<T extends Actor> {
    private final int delay;
    protected final List<CombatListener<? super T>> listeners;

    private CombatEvent<T> next;
    private int ticks;
    private boolean active;

    protected CombatEvent(List<CombatListener<? super T>> listeners, int delay) {
        this.listeners = listeners;
        this.delay = delay;
        active = true;
    }

    void tick() {
        ticks++;
    }

    public boolean canExecute() {
        return active && ticks > delay;
    }

    protected abstract void execute();

    boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    CombatEvent<T> getNext() {
        return next;
    }

    void setNext(CombatEvent<T> next) {
        this.next = next;
    }

}
