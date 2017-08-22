package net.edge.content.combat.events;

import net.edge.world.entity.actor.Actor;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class CombatChain<T extends Actor> extends CombatEvent<T> implements Iterable<CombatEvent<T>> {
    private CombatEvent<T> head;
    private CombatEvent<T> tail;

    private int size;

    private CombatChain() {
        super(null, 0);
    }

    public CombatChain<T> link(CombatEvent<T> event) {
        if (head == null) {
            head = event;
            tail = event;
        } else {
            tail.setNext(event);
            tail = event;
        }
        size++;
        return this;
    }

    public CombatChain<T> linkFirst(CombatEvent<T> event) {
        if (head == null) {
            head = tail = event;
        } else {
            event.setNext(head);
            head = event;
        }
        size++;
        return this;
    }

    @Override
    void tick() {
        for (CombatEvent<T> event : this) {
                event.tick();
                if (event.isActive() && !event.canExecute()) break;
        }
    }

    @Override
    public boolean canExecute() {
        return isActive() && head.canExecute();
    }

    @Override
    protected void execute() {
        head.execute();
        head = head.getNext();
        setActive(head != null);
    }

    @Override
    public String toString() {
        String builder = "";
        CombatEvent<T> next = head;
        while (next != null) {
            builder += next.getClass().getSimpleName();
            next = next.getNext();

            if (next != null) {
                builder += ", ";
            }
        }
        return builder;
    }

    public static <T extends Actor> CombatChain<T> create() {
        return new CombatChain<>();
    }

    @Override
    public Iterator<CombatEvent<T>> iterator() {
        return new EventIterator();
    }

    private class EventIterator implements Iterator<CombatEvent<T>> {
        private final int cachedSize = size;
        private CombatEvent<T> event = head;

        @Override
        public boolean hasNext() {
            return event != null;
        }

        @Override
        public CombatEvent<T> next() {
            if (size != cachedSize) {
                throw new ConcurrentModificationException("lul noob tried to modify a combat chain while it was iterating xD");
            }

            CombatEvent<T> next = event;
            event = event.getNext();
            return next;
        }

    }

}
