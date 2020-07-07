package com.rageps.world.locale.loc;

import com.rageps.world.entity.Entity;
import com.rageps.world.locale.Position;

import java.util.Arrays;
import java.util.List;

/**
 * The class that represents an area located anywhere in the world.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Area {
	
	/**
	 * Determines if the specified position is in this area.
	 * @param position the position to determine if in this area.
	 * @return {@code true} if the position is in this area, {@code false}
	 * otherwise.
	 */
	public abstract boolean inArea(Position position);
	
	/**
	 * Determines if the specified position is within this area.
	 * @param position the position to determine if within this area.
	 * @param distance the distance to check if within.
	 * @return {@code true} if the position is within this area, {@code false}
	 * otherwise.
	 */
	public abstract boolean isWithin(Position position, int distance);
	
	/**
	 * Generates a pseudo-random position from within this area.
	 * @return the random position that was generated.
	 * @throws UnsupportedOperationException by default, if this method is not overridden.
	 */
	public Position random() {
		throw new UnsupportedOperationException("No algorithm to generate a " + "pseudo-random position from this area!");
	}
	
	/**
	 * Determines if the specified position is in <b>all</b> of the specified
	 * areas.
	 * @param position the position to determine if in the areas.
	 * @param area the areas to determine if in this position.
	 * @return {@code true} if the position is in all of these areas,
	 * {@code false} otherwise.
	 */
	public static boolean inAllArea(Position position, Area... area) {
		return Arrays.stream(area).allMatch(l -> l.inArea(position));
	}
	
	/**
	 * Determines if the specified position is within <b>all</b> of the specified
	 * areas.
	 * @param position the position to determine if within the areas.
	 * @param distance the distance to check if within.
	 * @param area the areas to determine if in this position.
	 * @return {@code true} if the position is within any of these areas,
	 * {@code false} otherwise.
	 */
	public static boolean isAllWithin(Position position, int distance, Area... area) {
		return Arrays.stream(area).allMatch(l -> l.isWithin(position, distance));
	}
	
	/**
	 * Determines if the specified position is in <b>any</b> of the specified
	 * areas.
	 * @param position the position to determine if in the areas.
	 * @param area the areas to determine if in this position.
	 * @return {@code true} if the position is in any of these areas,
	 * {@code false} otherwise.
	 */
	public static boolean inAnyArea(Position position, List<Area> area) {
		return area.stream().anyMatch(l -> l.inArea(position));
	}
	
	/**
	 * Determines if the specified position is in <b>any</b> of the specified
	 * areas.
	 * @param position the position to determine if in the areas.
	 * @param areas the areas to determine if in this position.
	 * @return {@code true} if the position is in any of these areas,
	 * {@code false} otherwise.
	 */
	public static boolean inAnyArea(Position position, Area... areas) {
		return inAnyArea(position, Arrays.asList(areas));
	}
	
	/**
	 * Determines if the specified position is within <b>any</b> of the specified
	 * areas.
	 * @param position the position to determine if within the areas.
	 * @param distance the distance to check if within.
	 * @param area the areas to determine if in this position.
	 * @return {@code true} if the position is within any of these areas,
	 * {@code false} otherwise.
	 */
	public static boolean isAnyWithin(Position position, int distance, Area... area) {
		return Arrays.stream(area).anyMatch(l -> l.isWithin(position, distance));
	}

	/**
	 * The position for the pest control area.
	 */
	public static final Position PEST_CONTROL = new Position(2662, 2648);
}
