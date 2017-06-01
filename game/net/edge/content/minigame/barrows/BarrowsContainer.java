package net.edge.content.minigame.barrows;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The container class for the barrows which holds fields that should be
 * modified.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarrowsContainer {
	
	/**
	 * The brothers this player has killed.
	 */
	private EnumSet<BarrowsData> killedBrothers = EnumSet.noneOf(BarrowsData.class);
	
	/**
	 * The current brother spawned.
	 */
	private Optional<BarrowBrother> current = Optional.empty();
	
	/**
	 * @return the killed brothers.
	 */
	public EnumSet<BarrowsData> getKilledBrothers() {
		return killedBrothers;
	}
	
	/**
	 * @return the current
	 */
	public Optional<BarrowBrother> getCurrent() {
		return current;
	}
	
	/**
	 * @param current the current to set
	 */
	public void setCurrent(Optional<BarrowBrother> current) {
		this.current = current;
	}
	
	/**
	 * @param current the current to set
	 */
	public void setCurrent(BarrowBrother current) {
		this.current = Optional.ofNullable(current);
	}
	
}
