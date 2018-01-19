package net.arrav.world.entity.actor.move.path;

import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

import java.util.*;

/**
 * Represents a {@code PathFinder} which uses the A* search algorithm(by passing obstacles).
 * @author Graham
 * @author Major | Suggestions, discussion
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AStarPathFinder extends PathFinder {
	
	/**
	 * The cost of moving in a straight line.
	 */
	private static final int COST_STRAIGHT = 10;
	
	public Path find(Position origin, Position destination, int size) {
		Map<Position, Node> nodes = new HashMap<>();
		
		int z = origin.getZ();
		int dist = (int) origin.getDistance(destination);
		Node start = new Node(origin);
		Node end = new Node(destination);
		start.setCost(1000);
		nodes.put(start.getPosition(), start);
		nodes.put(end.getPosition(), end);
		
		Set<Node> open = new HashSet<>();
		Queue<Node> sorted = new PriorityQueue<>();
		
		Node closest = start;
		boolean found = false;
		open.add(start);
		sorted.add(start);
		int process = dist * 5;
		do {
			process--;
			if(process <= 0)
				break;
			Node active = getCheapest(sorted);
			Position pos = active.getPosition();
			if(pos.getX() == destination.getX() && pos.getY() == destination.getY()) {
				found = true;
				break;
			}
			
			//World.getRegions().getRegion(pos).get().register(new GroundItem(new Item(995, active.getCost()), pos, World.get().getPlayer("avro").get()));
			
			open.remove(active);
			active.close();
			
			int x = pos.getX();
			int y = pos.getY();
			if(TraversalMap.isTraversableSouth(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x, y - 1, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableWest(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x - 1, y, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableNorth(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x, y + 1, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableEast(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x + 1, y, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableSouthWest(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x - 1, y - 1, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableNorthWest(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x - 1, y + 1, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableSouthEast(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x + 1, y - 1, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
			if(TraversalMap.isTraversableNorthEast(z, x, y, size)) {
				Node other = createIfAbsent(new Position(x + 1, y + 1, z), nodes);
				closest = compare(closest, end, active, other, open, sorted);
			}
			
		} while(!open.isEmpty());
		
		Deque<Position> shortest = new ArrayDeque<>();
		Node active = found ? end : closest;
		if(active != null && active.getParent() != null) {
			Position position = active.getPosition();
			while(!active.equals(start)) {
				shortest.addFirst(position);
				active = active.getParent();
				position = active.getPosition();
			}
		}
		return new Path(shortest);
	}
	
	/**
	 * Gets the cheapest open {@link Node} from the {@link Queue}.
	 * @param nodes The queue of nodes.
	 * @return The cheapest node.
	 */
	private Node getCheapest(Queue<Node> nodes) {
		Node node = nodes.peek();
		while(!node.isOpen()) {
			nodes.poll();
			node = nodes.peek();
		}
		
		return node;
	}
	
	/**
	 * Gets the closest node possible.
	 * @param nodes The queue of nodes.
	 * @return The closest cheapest node.
	 */
	private Node getClosest(Queue<Node> nodes) {
		Node node = nodes.peek();
		while(!node.isOpen()) {
			nodes.poll();
			node = nodes.peek();
		}
		return node;
	}
	
	/**
	 * Compares the two specified {@link Node}s, adding the check node to the
	 * open {@link Set} if the estimation is cheaper than the current cost.
	 * @param closest The closest node found.
	 * @param active The active node.
	 * @param check The node to compare the active node against.
	 * @param open The set of open nodes.
	 * @param sorted The sorted {@link Queue} of nodes.
	 * @return The closets node of all the search.
	 */
	private Node compare(Node closest, Node end, Node active, Node check, Set<Node> open, Queue<Node> sorted) {
		int cost = estimateDistance(check.getPosition(), end.getPosition());
		if(cost < closest.getCost()) {
			closest = check;
		}
		if(check.getCost() > cost) {
			open.remove(check);
			check.close();
		} else if(check.isOpen() && !open.contains(check)) {
			check.setCost(cost);
			check.setParent(active);
			open.add(check);
			sorted.add(check);
		}
		return closest;
	}
	
	/**
	 * Creates a {@link Node} and inserts it into the specified {@link Map} if
	 * one does not already exist, then returns that node.
	 * @param position The {@link Position}.
	 * @param nodes The map of positions to nodes.
	 * @return The node.
	 */
	private Node createIfAbsent(Position position, Map<Position, Node> nodes) {
		Node existing = nodes.get(position);
		if(existing == null) {
			existing = new Node(position);
			nodes.put(position, existing);
		}
		
		return existing;
	}
	
	/**
	 * A heuristic to estimate the distance between two positions.
	 * @param next The next check position.
	 * @param dest The destination position.
	 * @return The estimated distance between the two positions.
	 */
	private int estimateDistance(Position next, Position dest) {
		int deltaX = next.getX() - dest.getX();
		int deltaY = next.getY() - dest.getY();
		return (Math.abs(deltaX) + Math.abs(deltaY)) * COST_STRAIGHT;
	}
	
	/**
	 * Represents a node used by the A* algorithm.
	 * @author Graham
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 */
	private static final class Node implements Comparable<Node> {
		
		/**
		 * The parent node.
		 */
		private Node parent;
		
		/**
		 * The cost.
		 */
		private int cost;
		
		/**
		 * Whether or not this node is open.
		 */
		private boolean open = true;
		
		/**
		 * The position of this node.
		 */
		private final Position position;
		
		/**
		 * Constructs a new {@link Node}.
		 * @param position The position of this node.
		 */
		public Node(Position position) {
			this.position = Objects.requireNonNull(position);
		}
		
		/**
		 * Sets the parent.
		 * @param parent The parent.
		 */
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		/**
		 * Gets the parent node.
		 * @return The parent node.
		 */
		public Node getParent() {
			return parent;
		}
		
		/**
		 * Sets the cost.
		 * @param cost The cost.
		 */
		public void setCost(int cost) {
			this.cost = cost;
		}
		
		/**
		 * Gets the cost.
		 * @return The cost.
		 */
		public int getCost() {
			return cost;
		}
		
		/**
		 * Tests whether or not this node is open.
		 */
		public boolean isOpen() {
			return open;
		}
		
		/**
		 * Closes this node.
		 */
		public void close() {
			open = false;
		}
		
		/**
		 * Returns the position of this node.
		 */
		public Position getPosition() {
			return position;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cost;
			result = prime * result + Objects.hashCode(parent);
			result = prime * result + position.hashCode();
			result = prime * result + Boolean.hashCode(open);
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Node) {
				Node other = (Node) obj;
				return cost == other.cost && Objects.equals(parent, other.parent) && position.same(other.position) && open == other.open;
			}
			
			return false;
		}
		
		@Override
		public int compareTo(Node node) {
			return cost - node.cost;
		}
		
	}
	
}

