package net.arrav.content.skill.slayer;

import net.arrav.world.locale.Position;

/**
 * Represents a single slayer location, which basically chains the price and
 * location to teleport to your slayer assignment.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class SlayerLocationPolicy {
	
	/**
	 * The position to teleport the player to.
	 */
	private final Position[] positions;
	
	/**
	 * The price to teleport to the task.
	 */
	private final int price;
	
	/**
	 * Constructs a new {@link SlayerLocationPolicy}.
	 * @param positions {@link #positions}.
	 * @param price     {@link #price}.
	 */
	public SlayerLocationPolicy(Position[] positions, int price) {
		this.positions = positions;
		this.price = price;
	}
	
	/**
	 * @return {@link #positions}.
	 */
	public Position[] getPositions() {
		return positions;
	}
	
	/**
	 * @return {@link #price}.
	 */
	public int getPrice() {
		return price;
	}
}
