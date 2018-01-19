package net.arrav.action;

/**
 * Initializes some specific events.
 */
public abstract class ActionInitializer {
	
	public ActionInitializer() {
		init();
	}
	
	public abstract void init();
	
}
