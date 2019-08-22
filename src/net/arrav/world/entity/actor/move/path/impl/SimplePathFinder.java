package net.arrav.world.entity.actor.move.path.impl;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.move.path.Path;
import net.arrav.world.entity.actor.move.path.PathFinder;
import net.arrav.world.locale.Position;

import java.util.ArrayDeque;
import java.util.Deque;

import static net.arrav.world.Direction.*;

/**
 * Represents a simple path finder which determines a straight path to the first blocked tile or it's destination.
 * Mostly used by {@link Mob} following and movement.
 * @author Artem Batutin
 */
public final class SimplePathFinder extends PathFinder {
	/**
	 * A default method to find a path for the specified {@link Actor}.
	 * @param character The character to find the path for.
	 * @param destination The destination of the path.
	 * @return A {@link Deque} of {@link Position steps} to the specified destination.
	 */
	public Path find(Actor character, Position destination) {
		return find(character.getPosition(), destination, character.size());
	}
	
	/**
	 * A default method to find a path for the specified position.
	 * @param origin The original start position.
	 * @param destination The destination of the path.
	 * @param size The entity's size.
	 * @return A {@link Deque} of {@link Position steps} to the specified destination.
	 */
	public Path find(Position origin, Position destination, int size) {
		int approximation = (int) (origin.getLongestDelta(destination) * 1.5);
		Deque<Position> positions = new ArrayDeque<>(approximation);
		return new Path(addWalks(origin, destination, size, positions));
	}
	
	/**
	 * Performs the path finding calculations to find the path using the A* algorithm.
	 * @param origin The path finder's start position.
	 * @param destination The path finder's destination.
	 * @param size The entity's size.
	 * @param positions The current searched deque of moves.
	 * @return The path to pursue to reach the destination.
	 */
	private Deque<Position> addWalks(Position origin, Position destination, int size, Deque<Position> positions) {
		Position current = origin;
		
		while(!current.same(destination)) {
			Node node = new Node(current, Position.delta(destination, current));
			int x = node.getPosition().getX(), y = node.getPosition().getY();
			int dx = node.getDelta().getX(), dy = node.getDelta().getY();
			int height = node.getPosition().getZ();
			boolean move = false;//Prioritizing diagonals.
			boolean exit = true;//Exiting flag.
			
			if(dx > 0 && dy > 0) {
				while((dx-- > 0 && dy-- > 0)) {
					if(traversable(current, size, SOUTH_WEST)) {
						current = new Position(--x, --y, height);
						positions.addLast(current);
						move = true;
						exit = false;
					} else {
						break;
					}
				}
			} else if(dx < 0 && dy < 0) {
				while((dx++ < 0 && dy++ < 0)) {
					if(traversable(current, size, NORTH_EAST)) {
						current = new Position(++x, ++y, height);
						positions.addLast(current);
						move = true;
						exit = false;
					} else {
						break;
					}
				}
			} else if(dx < 0 && dy > 0) {
				while((dx++ < 0 && dy-- > 0)) {
					if(traversable(current, size, SOUTH_EAST)) {
						current = new Position(++x, --y, height);
						positions.addLast(current);
						move = true;
						exit = false;
					} else {
						break;
					}
				}
			} else if(dx > 0 && dy < 0) {
				while((dx-- > 0 && dy++ < 0)) {
					if(traversable(current, size, NORTH_WEST)) {
						current = new Position(--x, ++y, height);
						positions.addLast(current);
						move = true;
						exit = false;
					} else {
						break;
					}
				}
			}
			
			if(dy > 0 && !move) {
				while(dy-- > 0) {
					if(traversable(current, size, SOUTH)) {
						current = new Position(x, --y, height);
						positions.addLast(current);
						exit = false;
					} else {
						break;
					}
				}
			} else if(dy < 0 && !move) {
				while(dy++ < 0) {
					if(traversable(current, size, NORTH)) {
						current = new Position(x, ++y, height);
						positions.addLast(current);
						exit = false;
					} else {
						break;
					}
				}
			}
			if(dx > 0 && !move) {
				while(dx-- > 0) {
					if(traversable(current, size, WEST)) {
						current = new Position(--x, y, height);
						positions.addLast(current);
						exit = false;
					} else {
						break;
					}
				}
			} else if(dx < 0 && !move) {
				while(dx++ < 0) {
					if(traversable(current, size, EAST)) {
						current = new Position(++x, y, height);
						positions.addLast(current);
						exit = false;
					} else {
						break;
					}
				}
			}
			
			if(exit) {
				break;
			}
			
		}
		return positions;
	}
	
	/**
	 * Represents a node used by this simple algorithm.
	 */
	private static final class Node {
		
		/**
		 * The current node position.
		 */
		private Position position;
		
		/**
		 * The established delta position.
		 */
		private Position delta;
		
		/**
		 * Constructs {@code Entity}.
		 * @param position The current position.
		 * @param delta The delta position to move.
		 */
		public Node(Position position, Position delta) {
			this.position = position;
			this.delta = delta;
		}
		
		/**
		 * Gets the current node's position.
		 * @return node's position.
		 */
		public Position getPosition() {
			return position;
		}
		
		/**
		 * Gets the delta position.
		 * @return delta position.
		 */
		public Position getDelta() {
			return delta;
		}
		
	}
	
}