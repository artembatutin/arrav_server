package net.edge.world.model.node.entity.move.path;

import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Direction;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.region.TraversalMap;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents a simple path finder which determines a straight path to the first blocked tile or it's destination.
 * Mostly used by {@link Npc} following and movement.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcPathWalker extends PathFinder {
	
	/**
	 * Constructs the {@code SimplePathFinder} with the specified traversal map.
	 * @param traversalMap The traversal map to use.
	 */
	public NpcPathWalker(TraversalMap traversalMap) {
		super(traversalMap);
	}
	
	/**
	 * A default method to find a path for the specified {@link EntityNode}.
	 * @param character   The character to find the path for.
	 * @param destination The destination of the path.
	 * @return A {@link Deque} of {@link Position steps} to the specified destination.
	 */
	public Path find(EntityNode character, Position destination) {
		return find(character.getPosition(), destination, character.size());
	}
	
	/**
	 * A default method to find a path for the specified position.
	 * @param origin      The original start position.
	 * @param destination The destination of the path.
	 * @param size        The entity's size.
	 * @return A {@link Deque} of {@link Position steps} to the specified destination.
	 */
	public Path find(Position origin, Position destination, int size) {
		int approximation = (int) (origin.getLongestDelta(destination) * 1.5);
		Deque<Position> positions = new ArrayDeque<>(approximation);
		return new Path(addWalks(origin, destination, size, positions));
	}
	
	/**
	 * Performs the path finding calculations to find the path using the A* algorithm.
	 * @param origin      The path finder's start position.
	 * @param destination The path finder's destination.
	 * @param size        The entity's size.
	 * @param positions   The current searched deque of moves.
	 * @return The path to pursue to reach the destination.
	 */
	private Deque<Position> addWalks(Position origin, Position destination, int size, Deque<Position> positions) {
		Position current = origin;
		
		exit:
		while(!current.same(destination)) {
			Node node = new Node(current, Position.delta(destination, current));
			int x = node.getPosition().getX(), y = node.getPosition().getY();
			int dx = node.getDelta().getX(), dy = node.getDelta().getY();
			int height = node.getPosition().getZ();
			boolean move = false;//Prioritizing diagonals.
			boolean exit = true;//Exiting flag.
			
			if(dx > 0 && dy > 0) {
				while((dx-- > 0 && dy-- > 0)) {
					if(traversable(current, size, Direction.SOUTH_WEST)) {
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
					if(traversable(current, size, Direction.NORTH_EAST)) {
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
					if(traversable(current, size, Direction.SOUTH_EAST)) {
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
					if(traversable(current, size, Direction.NORTH_WEST)) {
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
					if(traversable(current, size, Direction.SOUTH)) {
						current = new Position(x, --y, height);
						positions.addLast(current);
						exit = false;
					} else {
						break;
					}
				}
			} else if(dy < 0 && !move) {
				while(dy++ < 0) {
					if(traversable(current, size, Direction.NORTH)) {
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
					if(traversable(current, size, Direction.WEST)) {
						current = new Position(--x, y, height);
						positions.addLast(current);
						exit = false;
					} else {
						break;
					}
				}
			} else if(dx < 0 && !move) {
				while(dx++ < 0) {
					if(traversable(current, size, Direction.EAST)) {
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
		 * Constructs {@code Node}.
		 * @param position The current position.
		 * @param delta    The delta position to move.
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