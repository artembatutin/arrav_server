package net.edge.world.entity.actor.move.path;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Direction;
import net.edge.world.entity.actor.move.path.distance.Distance;

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
	 * The Heuristic used by this {@code PathFinder}.
	 */
	private final Distance heuristic;

	private final Actor character;

	private final Object2ObjectArrayMap<Position, Node> nodes = new Object2ObjectArrayMap<>();

	private final Set<Node> open = new HashSet<>();

	private final Queue<Node> sorted = new PriorityQueue<>();

	private final Deque<Position> shortest = new ArrayDeque<>();

	/**
	 * Constructs a new {@link AStarPathFinder} with the specified traversal tool.mapviewer.
	 */
	public AStarPathFinder(Actor character, Distance heuristic) {
		this.character = character;
		this.heuristic = heuristic;
	}
	
	/**
	 * A default method to find a path for the specified {@link Actor}.
	 * @param destination The destination of the path.
	 * @return A {@link Deque} of {@link Position steps} to the specified destination.
	 */
	public Path find(Position destination) {
		Position origin = character.getPosition();
		if(origin.getZ() != destination.getZ())
			return new Path(null);
		return find(new Position(origin.getX(), origin.getY(), origin.getZ()), destination, character.size());
	}
	
	/**
	 * Performs the path finding calculations to find the path using the A* algorithm.
	 * @param origin The origin Position.
	 * @param target The target Position.
	 * @param size   The amount of positions the travers takes up.
	 * @return The path to pursue to reach the destination.
	 */
	@Override
	public Path find(Position origin, Position target, int size) {
		if(origin.getZ() != target.getZ())
			return new Path(null);
		nodes.clear();
		Node start = new Node(origin), end = new Node(target);
		nodes.put(origin, start);
		nodes.put(target, end);
		
		open.clear();
		sorted.clear();
		open.add(start);
		sorted.add(start);
		Node active;
		
		int distance = (int) origin.getDistance(target);
		boolean found = false;
		int count = 0, count2 = 0;
		do {
			active = getCheapest(sorted);
			Position position = active.getPosition();
			if(position.same(target)) {
				found = true;
				break;
			}
			if(count++ > 32 * 20 + 1) {
				break;
			}
			open.remove(active);
			active.close();
			
			for(Direction direction : Direction.VALUES) {
				if(direction != Direction.NONE) {
					Position move = position.move(direction);
					if(traversable(position, size, direction) && traversable(move, size, Direction.valueOf(direction.getOpposite()))) {
						Node node = nodes.computeIfAbsent(move, Node::new);
						compare(active, node, open, sorted, heuristic);
					}
				}
				
			}
		} while(!open.isEmpty() && sorted.size() < distance * 20);

		shortest.clear();
		if(found) {
			if(end.hasParent())
				active = end;
			if(active.hasParent()) {
				Position position = active.getPosition();
				
				while(!origin.same(position)) {
					shortest.addFirst(position);
					active = active.getParent(); // If the target has a parent then all of the others will.
					position = active.getPosition();
					if(count2++ > 100) {
						break;
					}
				}
			}
		} else
			return null;
		return new Path(shortest);
	}
	
	/**
	 * Compares the two specified {@link Node}s, adding the other node to the openShop {@link Set} if the estimation is
	 * cheaper than the current cost.
	 * @param active    The active node.
	 * @param other     The node to compare the active node against.
	 * @param open      The set of openShop nodes.
	 * @param sorted    The sorted {@link Queue} of nodes.
	 * @param heuristic The {@link Distance} used to estimate the cost of the node.
	 */
	private void compare(Node active, Node other, Set<Node> open, Queue<Node> sorted, Distance heuristic) {
		int cost = active.getCost() + heuristic.calculate(active.getPosition(), other.getPosition());
		if(other.getCost() > cost) {
			open.remove(other);
			other.close();
		} else if(other.isOpen() && !open.contains(other)) {
			other.setCost(cost);
			other.setParent(active);
			open.add(other);
			sorted.add(other);
		}
	}
	
	/**
	 * Gets the cheapest openShop {@link Node} from the {@link Queue}.
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
	
}

/**
 * A {@code Entity} representing a weighted {@link Position}.
 * @author Graham
 * @author Major
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
final class Node implements Comparable<Node> {
	
	/**
	 * The cost of this {@code Entity}.
	 */
	private int cost;
	
	/**
	 * Whether or not this {@code Entity} is openShop.
	 */
	private boolean open = true;
	
	/**
	 * The parent {@code Entity} of this Entity.
	 */
	private Optional<Node> parent = Optional.empty();
	
	/**
	 * The Position of this {@code Entity}.
	 */
	private final Position position;
	
	/**
	 * Creates the {@code Entity} with the specified {@link Position} and cost.
	 * @param position The Position.
	 */
	public Node(Position position) {
		this(position, 0);
	}
	
	/**
	 * Creates the {@code Entity} with the specified {@link Position} and cost.
	 * @param position The Position.
	 * @param cost     The cost of the Entity.
	 */
	public Node(Position position, int cost) {
		this.position = position;
		this.cost = cost;
	}
	
	/**
	 * Sets the cost of this Entity.
	 * @param cost The cost.
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Gets the cost of this Entity.
	 * @return The cost.
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * Closes this Entity.
	 */
	public void close() {
		open = false;
	}
	
	/**
	 * Returns whether or not this {@link Node} is openShop.
	 * @return {@code true} if this Entity is openShop, otherwise {@code false}.
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Sets the parent Entity of this Entity.
	 * @param parent The parent Entity. May be {@code null}.
	 */
	public void setParent(Node parent) {
		this.parent = Optional.ofNullable(parent);
	}
	
	/**
	 * Gets the parent Entity of this Entity.
	 * @return The parent Entity.
	 * @throws NoSuchElementException If this Entity does not have a parent.
	 */
	public Node getParent() {
		return parent.get();
	}
	
	/**
	 * Returns whether or not this Entity has a parent Entity.
	 * @return {@code true} if this Entity has a parent Entity, otherwise {@code false}.
	 */
	public boolean hasParent() {
		return parent.isPresent();
	}
	
	/**
	 * Gets the {@link Position} this Entity represents.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Compares the {@code Entity}'s cost with another.
	 * @param other The other Entity to check.
	 * @return The differential Integer.
	 */
	@Override
	public int compareTo(Node other) {
		return Integer.compare(cost, other.cost);
	}
	
	/**
	 * Gets the condition if the Entity equals another object.
	 * @param obj The object to be checked.
	 * @return {@code true} if it's the same as the object, {@code false} otherwise.
	 */
	@Override
	public final boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		return getPosition().getX() == other.getPosition().getX() && getPosition().getY() == other.getPosition().getY() && getPosition().getZ() == other.getPosition().getZ() && getCost() == other.getCost() && getParent().getCost() == other.getParent().getCost() && getParent().getPosition().getX() == other.getParent().getPosition().getX() && getParent().getPosition().getY() == other.getParent().getPosition().getY() && getParent().getPosition().getZ() == other.getPosition().getZ();
	}
	
	/**
	 * Gets the node's hash code.
	 * @return hash code.
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position.getX();
		result = prime * result + position.getY();
		result = prime * result + position.getZ();
		if(parent.isPresent()) {
			Position p = parent.get().getPosition();
			result = prime * result + p.getX();
			result = prime * result + p.getY();
			result = prime * result + p.getZ();
		}
		result = prime * result + cost;
		return result;
	}
	
}

