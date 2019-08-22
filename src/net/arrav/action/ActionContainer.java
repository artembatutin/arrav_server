package net.arrav.action;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

/**
 * A container to handle specific {@link Action}s.
 * @author Artem Batutin
 */
public class ActionContainer<E extends Action> {
	
	/**
	 * All of the registered events.
	 */
	private final Int2ObjectArrayMap<E> events = new Int2ObjectArrayMap<>();
	
	/**
	 * Registering an listener to the container.
	 * @param i the identifier.
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
	 * Gets the {@link Action} from this container.
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
	public Int2ObjectArrayMap<E> events() {
		return events;
	}
	
}
