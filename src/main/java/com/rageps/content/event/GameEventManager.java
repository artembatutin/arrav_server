package com.rageps.content.event;

import com.rageps.task.Task;
import com.rageps.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The event manager.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class GameEventManager {

    /**
     * Logger for the game event manager.
     */
    private static final Logger log = LogManager.getLogger();


    /**
     * The current events.
     */
    private static final HashMap<String, GameEvent> events = new HashMap<>(0);

    /**
     * The delay to start the events after the events manager is created.
     */
    private static final long startDelay = TimeUnit.SECONDS.toMillis(90);

    /**
     * The instant the events got loaded
     */
    private static long loadInstant;

    /**
     * Return the game events.
     *
     * @return the events
     */
    public static HashMap<String, GameEvent> getEvents() {
        return events;
    }

    /**
     * Loads all the events.
     */
    public static void loadEvents() {
        loadInstant = System.currentTimeMillis();
        try {
            Set<Class<? extends GameEvent>> clazzSet = new Reflections(GameEvent.class.getPackage().getName()).getSubTypesOf(GameEvent.class);
            for(Class<?> clazz : clazzSet ) {
                GameEvent event = (GameEvent) clazz.newInstance();
                events.put(event.name(), event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Loaded " + events.size() + " game events.");
        log.info("Starting all game events in " + startDelay + " seconds.");
        World.get().getTask().submit(eventTask);
    }

    /**
     * The task to process all events handling.
     */
    private static final Task eventTask = new Task(1) {
        @Override
        protected void execute() {

            if (System.currentTimeMillis() - loadInstant < startDelay) {
                return;
            }

            if (events.isEmpty()) {
                return;
            }

            for (GameEvent event : events.values()) {
                if (event.isActive()) {
                    if (event.isOver()) {
                        event.end();
                        event.setActive(false);
                        continue;
                    }
                    event.process();
                    event.eventMessages(event.getEventDuration() + event.getLastEventInstant() - System.currentTimeMillis());
                } else {
                    if (System.currentTimeMillis() - event.getLastEventInstant() > event.getDelayBetweenEvents()) {
                        if (event.canStart() && event.start()) {
                            event.setActive(true);
                            event.setLastEventInstant(System.currentTimeMillis());
                        }
                    } else {
                        event.preEventMessages(event.getDelayBetweenEvents() + event.getLastEventInstant() - System.currentTimeMillis());
                    }

                }
            }
        }
    };
}
