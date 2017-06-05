package net.edge.event;

/**
 * Initializes some specific events.
 */
public abstract class EventInitializer {
	
	public EventInitializer() {
		init();
	}
	
	public abstract void init();
	
}
