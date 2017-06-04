package net.edge.event;

/**
 * Initializes some specific events.
 */
public abstract class EventInitializer {
	
	public EventInitializer() {
		init();
	}
	
	protected abstract void init();
	
}
