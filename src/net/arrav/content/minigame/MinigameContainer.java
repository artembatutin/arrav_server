package net.arrav.content.minigame;

import net.arrav.content.minigame.barrows.BarrowsContainer;

/**
 * The container which holds functionality for player-based minigames, this class should
 * hold instances of other containers which hold values that should be modified.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class MinigameContainer {
	
	/**
	 * The barrows container which holds fields that should be modified.
	 */
	private final BarrowsContainer barrows = new BarrowsContainer();
	
	/**
	 * @return the barrows.
	 */
	public BarrowsContainer getBarrowsContainer() {
		return barrows;
	}
	
}
