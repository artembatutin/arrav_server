package net.edge.world.node.entity.move.path;

import com.google.common.base.Preconditions;
import net.edge.locale.Position;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.node.region.TraversalMap;

/**
 * An algorithm used to find a path between two {@link Position}s.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class PathFinder {
	/**
	 * Finds a valid path from the origin {@link Position} to the target one.
	 * @param origin The origin Position.
	 * @param target The target Position.
	 * @param size   The entity's size.
	 * @return The path containing the Positions to go through.
	 */
	public abstract Path find(Position origin, Position target, int size);
	
	/**
	 * Returns whether or not a {@link Position} walking one step in any of the specified {@link Direction}s would lead
	 * to is traversable.
	 * @param current    The current Position.
	 * @param size       The entity's size.
	 * @param directions The Directions that should be checked.
	 * @return {@code true} if any of the Directions lead to a traversable tile, otherwise {@code false}.
	 */
	protected boolean traversable(Position current, int size, Direction... directions) {
		TraversalMap traversalMap = World.getTraversalMap();
		for(Direction direction : directions) {
			if(!traversalMap.isTraversable(current, direction, size)) {
				return false;// not traversable
			}
		}
		return true;
	}
	
	/**
	 * Returns whether or not a {@link Position} is traversable to the direction of another {@link Position}.
	 * @param current The current Position.
	 * @param going   The position to which we are going.
	 * @return {@code true} if any of the Directions lead to a traversable tile, otherwise {@code false}.
	 */
	boolean traversable(Position current, Position going, int size) {
		Direction first = Direction.fromDeltas(Position.delta(current, going));
		TraversalMap traversalMap = World.getTraversalMap();

		return traversalMap.isTraversable(current, first, size);
	}
	
	/**
	 * Returns whether or not a {@link Position} shooting projectile to another {@link Position} would lead
	 * to is traversable.
	 * @param current The current Position.
	 * @param going   The position to which we are going.
	 * @return {@code true} if any of the Directions lead to a projectile traversable tile, otherwise {@code false}.
	 */
	boolean projectileCheck(Position current, Position going) {
		Direction first = Direction.fromDeltas(Position.delta(going, current));
		Direction second = Direction.fromDeltas(Position.delta(current, going));
		TraversalMap traversalMap = World.getTraversalMap();

		return (traversalMap.isTraversable(current, second, true) && traversalMap.isTraversable(going, first, true));
	}
	
}