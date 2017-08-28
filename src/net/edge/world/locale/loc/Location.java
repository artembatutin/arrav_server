package net.edge.world.locale.loc;

import net.edge.world.entity.Entity;
import net.edge.world.locale.Position;
import net.edge.world.locale.area.AreaManager;

import java.util.Arrays;
import java.util.List;

/**
 * The class that represents an area located anywhere in the world.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Location {

	/**
	 * Determines if the specified position is in this location.
	 *
	 * @param position the position to determine if in this location.
	 * @return {@code true} if the position is in this location, {@code false}
	 * otherwise.
	 */
	public abstract boolean inLocation(Position position);

	/**
	 * Determines if the specified position is within this location.
	 *
	 * @param position the position to determine if within this location.
	 * @param distance the distance to check if within.
	 * @return {@code true} if the position is within this location, {@code false}
	 * otherwise.
	 */
	public abstract boolean isWithin(Position position, int distance);

	/**
	 * Generates a pseudo-random position from within this location.
	 *
	 * @return the random position that was generated.
	 * @throws UnsupportedOperationException by default, if this method is not overridden.
	 */
	public Position random() {
		throw new UnsupportedOperationException("No algorithm to generate a " + "pseudo-random position from this location!");
	}

	/**
	 * Determines if the specified position is in <b>all</b> of the specified
	 * locations.
	 *
	 * @param position the position to determine if in the locations.
	 * @param location the locations to determine if in this position.
	 * @return {@code true} if the position is in all of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inAllLocation(Position position, Location... location) {
		return Arrays.stream(location).allMatch(l -> l.inLocation(position));
	}

	/**
	 * Determines if the specified position is within <b>all</b> of the specified
	 * locations.
	 *
	 * @param position the position to determine if within the locations.
	 * @param distance the distance to check if within.
	 * @param location the locations to determine if in this position.
	 * @return {@code true} if the position is within any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean isAllWithin(Position position, int distance, Location... location) {
		return Arrays.stream(location).allMatch(l -> l.isWithin(position, distance));
	}

	/**
	 * Determines if the specified position is in <b>any</b> of the specified
	 * locations.
	 *
	 * @param position the position to determine if in the locations.
	 * @param location the locations to determine if in this position.
	 * @return {@code true} if the position is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inAnyLocation(Position position, List<Location> location) {
		return location.stream().anyMatch(l -> l.inLocation(position));
	}

	/**
	 * Determines if the specified position is in <b>any</b> of the specified
	 * locations.
	 *
	 * @param position  the position to determine if in the locations.
	 * @param locations the locations to determine if in this position.
	 * @return {@code true} if the position is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inAnyLocation(Position position, Location... locations) {
		return inAnyLocation(position, Arrays.asList(locations));
	}

	/**
	 * Determines if the specified position is within <b>any</b> of the specified
	 * locations.
	 *
	 * @param position the position to determine if within the locations.
	 * @param distance the distance to check if within.
	 * @param location the locations to determine if in this position.
	 * @return {@code true} if the position is within any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean isAnyWithin(Position position, int distance, Location... location) {
		return Arrays.stream(location).anyMatch(l -> l.isWithin(position, distance));
	}

	/**
	 * Determines if {@code entity} is in any of the fun pvp locations.
	 *
	 * @param entity the entity to determine if in the locations.
	 * @return {@code true} if the entity is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inFunPvP(Entity entity) {
		return AreaManager.get().inArea(entity.getPosition(), "FUN_PVP");
	}

	/**
	 * Determines if {@code entity} is in any of the multicombat locations.
	 *
	 * @param entity the entity to determine if in the locations.
	 * @return {@code true} if the entity is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inMultiCombat(Entity entity) {
		return AreaManager.get().inMulti(entity);
	}

	/**
	 * Determines if {@code entity} is in any of the wilderness locations.
	 *
	 * @param entity the entity to determine if in the locations.
	 * @return {@code true} if the entity is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inWilderness(Entity entity) {
		return inWilderness(entity.getPosition());
	}

	/**
	 * Determines if {@code pos} is in any of the wilderness locations.
	 *
	 * @param pos the position to determine if in the locations.
	 * @return {@code true} if the node is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inWilderness(Position pos) {
		return AreaManager.get().inArea(pos, "WILDERNESS") && !AreaManager.get().inArea(pos, "CLAN_WARS");
	}

	/**
	 * Determines if {@code entity} is in any of the duel locations.
	 *
	 * @param entity the entity to determine if in the locations.
	 * @return {@code true} if the entity is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inDuelArena(Entity entity) {
		return AreaManager.get().inArea(entity.getPosition(), "DUEL_ARENA");
	}

	/**
	 * Determines if {@code entity} is in any of the godwars locations.
	 *
	 * @param entity the entity to determine if in the locations.
	 * @return {@code true} if the entity is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean inGodwars(Entity entity) {
		return AreaManager.get().inArea(entity.getPosition(), "GODWARS");
	}

	/**
	 * Determines if {@code entity} is in any of the home locations.
	 *
	 * @param entity the entity to determine if in the locations.
	 * @return {@code true} if the entity is in any of these locations,
	 * {@code false} otherwise.
	 */
	public static boolean isAtHome(Entity entity) {
		return AreaManager.get().inArea(entity.getPosition(), "HOME");
	}

	/**
	 * The position for the pest control location.
	 */
	public static final Position PEST_CONTROL = new Position(2662, 2648);
}
