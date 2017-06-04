package net.edge.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A container to handle specific {@link Event}s.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class EventContainer<E extends Event> {
	
	/**
	 * All of the registered events.
	 */
	private final Map<Integer, E> events = new HashMap<>();
	
	/**
	 * Registering an listener to the container.
	 * @param i     the identifier.
	 * @param listener listener to register.
	 */
	public void register(int i, E listener) {
		events.put(i, listener);
	}
	
	/**
	 * Registering the listener from the container.
	 * @param i the identifier.
	 */
	public void deregister(int i) {
		events.remove(i);
	}
	
	/**
	 * Gets the {@link Event} from this container.
	 * @param i the indetifier.
	 * @return the event.
	 */
	public E get(int i) {
		if(!events.containsKey(i))
			return null;
		return events.get(i);
	}
	
	/**
	 * Gets the events.
	 * @return events.
	 */
	public Map<Integer, E> events() {
		return events;
	}

}
