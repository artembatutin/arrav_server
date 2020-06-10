package com.rageps.world.entity;

import com.rageps.task.Task;
import com.rageps.util.CallBack;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;

import static java.util.Objects.requireNonNull;

/**
 * The parent class that represents anything that can be interacted with in the {@link World}.
 * This includes {@link GroundItem}s, {@link GameObject}s, {@link Player}s, and {@link Mob}s.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Entity {
	
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
	private final EntityType type;
	
	/**
	 * The current of this node.
	 */
	private EntityState state = EntityState.IDLE;
	
	/**
	 * Creates a new {@link Entity}.
	 * @param position the position of this node in the world.
	 * @param type the type of node that this node is.
	 */
	public Entity(Position position, EntityType type) {
		this.type = requireNonNull(type);
		this.setPosition(position);
		initialize();
	}
	
	/**
	 * Fired on each game tick, perhaps has some few exceptions.
	 */
	public void update() {
	
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
	 * Sets the value for {@link Entity#position}.
	 * @param position the new value to set.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Gets the type of node that this node is.
	 * @return the type of node that this node is.
	 */
	public final EntityType getType() {
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
	public final EntityState getState() {
		return state;
	}
	
	/**
	 * Sets the value for {@link #state}. When a state is set, a corresponding listener of either {@code onIdle()}, {@code
	 * onActive()}, or {@code onInactive()} will be fired. If the value being set is equal to the current value, an exception
	 * will be thrown.
	 * @param state The state to set, cannot be {@code null} or {@code IDLE}.
	 * @throws IllegalArgumentException If the value being set is equal to the current value.
	 */
	public final void setState(EntityState state) {
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

	/** Delay an action for the next cycle **/
	protected void delay(CallBack callBack) {
		delay(1,  callBack);
	}

	/**
	 * Delay an action for a given {@code delay} **; protected void delay(int
	 * delay, CallBack callBack) { delay(delay, 0, callBack); }
	 *
	 * /** Delay an action for a given {@code delay} and repeat it for an amount
	 * of times based on {@code repeatCount}
	 **/
	public void delay(int delay, CallBack callBack) {
		World.get().submit(new Task(delay, false) {
			@Override
			protected void execute() {
				callBack.callBack();
				cancel();
			}
		});
	}
	
	/**
	 * Gets the region on which the entity is standing.
	 * @return the region of this object.
	 */
	public Region getRegion() {
		return World.getRegions().getRegion(getPosition());
	}
	
}