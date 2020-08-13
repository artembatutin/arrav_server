package com.rageps.world.entity.actor.move.path;

import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Direction;

/**
 * An algorithm used to find a path between two {@link Position}s.
 * @author Artem Batutin
 */
public abstract class PathFinder {
	/**
	 * Finds a valid path from the origin {@link Position} to the target one.
	 * @param origin The origin Position.
	 * @param target The target Position.
	 * @param size The entity's size.
	 * @return The path containing the Positions to go through.
	 */
	public abstract Path find(Position origin, Position target, int size);
	
	/**
	 * Returns whether or not a {@link Position} walking one step in any of the specified {@link Direction}s would lead
	 * to is traversable.
	 * @param current The current Position.
	 * @param size The entity's size.
	 * @param directions The Directions that should be checked.
	 * @return {@code true} if any of the Directions lead to a traversable tile, otherwise {@code false}.
	 */
	protected boolean traversable(Position current, int size, Direction... directions) {
		for(Direction direction : directions) {
			if(!TraversalMap.isTraversable(current, direction, size)) {
				return false;// not traversable
			}
		}
		return true;
	}
	
	/**
	 * Returns whether or not a {@link Position} is traversable to the direction of another {@link Position}.
	 * @param current The current Position.
	 * @param going The position to which we are going.
	 * @return {@code true} if any of the Directions lead to a traversable tile, otherwise {@code false}.
	 */
	boolean traversable(Position current, Position going, int size) {
		Direction first = Direction.fromDeltas(Position.delta(current, going));
		return TraversalMap.isTraversable(current, first, size);
	}
	
	/**
	 * Returns whether or not a {@link Position} shooting projectile to another {@link Position} would lead
	 * to is traversable.
	 * @param current The current Position.
	 * @param going The position to which we are going.
	 * @return {@code true} if any of the Directions lead to a projectile traversable tile, otherwise {@code false}.
	 */
	boolean projectileCheck(Position current, Position going) {
		Direction first = Direction.fromDeltas(Position.delta(going, current));
		Direction second = Direction.fromDeltas(Position.delta(current, going));
		return (TraversalMap.isTraversable(current, second, true) && TraversalMap.isTraversable(going, first, true));
	}
	
}