package net.edge.world.node;

import net.edge.World;
import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.ItemNode;
import net.edge.world.object.ObjectNode;
import net.edge.world.node.region.Region;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * The parent class that represents anything that can be interacted with in the {@link World}.
 * This includes {@link ItemNode}s, {@link ObjectNode}s, {@link Player}s, and {@link Npc}s.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Node {
	
	/**
	 * The position of this node in the world.
	 */
	private Position position;
	
	/**
	 * The instance of this node.
	 */
	private int instance;
	
	/**
	 * The type of node that this node is.
	 */
	private final NodeType type;
	
	/**
	 * The current of this node.
	 */
	private NodeState state = NodeState.IDLE;
	
	/**
	 * Creates a new {@link Node}.
	 * @param position the position of this node in the world.
	 * @param type     the type of node that this node is.
	 */
	public Node(Position position, NodeType type) {
		this.type = requireNonNull(type);
		this.setPosition(position);
		initialize();
	}
	
	/**
	 * Fired on each game tick, perhaps has some few exceptions.
	 */
	public void sequence() {
		
	}
	
	/**
	 * Fired when the state of this {@code Entity} is set to {@code IDLE}.
	 */
	public void initialize() {
		
	}
	
	/**
	 * Fired when the state of this {@code Entity} is set to {@code ACTIVE}.
	 */
	public abstract void register();
	
	/**
	 * Fired when the state of this {@code Entity} is set to {@code INACTIVE}.
	 */
	public abstract void dispose();
	
	@Override
	public String toString() {
		return "NODE[type= " + type + ", state= " + state.toString().toLowerCase() + ", instance= " + instance + "]";
	}
	
	/**
	 * Gets the position of this node in the world.
	 * @return the position of this node in the world.
	 */
	public final Position getPosition() {
		return position;
	}
	
	/**
	 * Sets the value for {@link Node#position}.
	 * @param position the new value to set.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Gets the type of node that this node is.
	 * @return the type of node that this node is.
	 */
	public final NodeType getType() {
		return type;
	}
	
	/**
	 * Gets the instance of this node.
	 * @return the instance.
	 */
	public final int getInstance() {
		return instance;
	}
	
	/**
	 * Sets the instance of tis node.
	 * @param instance the instance to set.
	 */
	public void setInstance(int instance) {
		this.instance = instance;
	}
	
	/**
	 * @return The current state that this {@code Entity} is in.
	 */
	public final NodeState getState() {
		return state;
	}
	
	/**
	 * Sets the value for {@link #state}. When a state is set, a corresponding listener of either {@code onIdle()}, {@code
	 * onActive()}, or {@code onInactive()} will be fired. If the value being set is equal to the current value, an exception
	 * will be thrown.
	 * @param state The state to set, cannot be {@code null} or {@code IDLE}.
	 * @throws IllegalArgumentException If the value being set is equal to the current value.
	 */
	public final void setState(NodeState state) {
		checkArgument(state != NodeState.IDLE, "IDLE state cannot be explicitly set");
		this.state = requireNonNull(state);
		switch(state) {
			case ACTIVE:
				register();
				break;
			case INACTIVE:
				dispose();
				break;
		}
	}
	
	/**
	 * Gets the region on which the object is standing.
	 * @return the region of this object.
	 */
	public Region getRegion() {
		return World.getRegions().getRegion(getPosition());
	}
	
}