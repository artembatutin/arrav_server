package net.arrav.world.entity.object;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.net.packet.out.SendObject;
import net.arrav.net.packet.out.SendObjectRemoval;
import net.arrav.task.Task;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.region.Region;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static net.arrav.world.entity.object.ObjectDirection.*;

/**
 * The node that represents an object anywhere in the world.
 * @author Artem Batutin
 */
public abstract class GameObject {
	
	/**
	 * The identification of this object.
	 */
	private int id;
	
	/**
	 * The {@link ObjectType} of this object.
	 */
	private final ObjectType type;
	
	/**
	 * The {@link ObjectDirection} of this object.
	 */
	private final ObjectDirection direction;
	
	/**
	 * Creates a new {@link GameObject}.
	 * @param id the identification of the object.
	 * @param direction the direction this object is facing.
	 * @param type the type of object that this is.
	 */
	public GameObject(int id, ObjectDirection direction, ObjectType type) {
		this.id = id;
		this.type = type;
		this.direction = direction;
	}
	
	/**
	 * Creates a new {@link GameObject} with the default {@code objectType}.
	 * @param id the identification of the object.
	 * @param direction the direction this object is facing.
	 */
	public GameObject(int id, ObjectDirection direction) {
		this(id, direction, ObjectType.GENERAL_PROP);
	}
	
	public void visible(boolean on) {
		Region r = getRegion();
		if(r != null) {
			ObjectList<Region> surrounding = r.getSurroundingRegions();
			if(surrounding != null) {
				for(Region s : surrounding) {
					if(s == null)
						continue;
					for(Player p : s.getPlayers()) {
						if(p == null)
							continue;
						if(getZ() == p.getPosition().getZ() && getInstance() == p.getInstance()) {
							if(on)
								p.out(new SendObject(this));
							else
								p.out(new SendObjectRemoval(this));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gets this object with the new id.
	 * @param id new direction to set.
	 * @return the copy of this object.
	 */
	public GameObject setId(int id) {
		remove();
		this.id = id;
		return this;
	}
	
	/**
	 * Gets a copy of this object with the new {@link ObjectDirection}.
	 * @param direction new direction to set.
	 * @return the copy of this object.
	 */
	public GameObject setDirection(ObjectDirection direction) {
		remove();
		return copy(direction);
	}
	
	/**
	 * Gets a copy of this object with the new {@link ObjectType}.
	 * @param objectType new type to set.
	 * @return the copy of this object.
	 */
	public GameObject setObjectType(ObjectType objectType) {
		remove();
		return copy(objectType);
	}
	
	/**
	 * Gets a copy of this object.
	 * @return the new object created.
	 */
	public abstract GameObject copy();
	
	/**
	 * Copying the object with a new specified {@link ObjectDirection}.
	 * @param direction the specified direction.
	 * @return the new object created.
	 */
	public abstract GameObject copy(ObjectDirection direction);
	
	/**
	 * Copying the object with a new specified {@link ObjectType}.
	 * @param type the specified type.
	 * @return the new object created.
	 */
	public abstract GameObject copy(ObjectType type);
	
	/**
	 * Returns the X coordinate of this object.
	 * @return object's X coordinate.
	 */
	public abstract int getX();
	
	/**
	 * Returns the Y coordinate of this object.
	 * @return object's Y coordinate.
	 */
	public abstract int getY();
	
	/**
	 * Returns the Z coordinate of this object.
	 * @return object's Z coordinate.
	 */
	public abstract int getZ();
	
	/**
	 * Gets the unpacked position of this object as {@link Position} instance.
	 * @return an position instance of this object.
	 */
	public abstract Position getPosition();
	
	/**
	 * Gets the region position of this object as {@link Position} instance.
	 * @return an regional position instance of this object.
	 */
	public Position getRegionPos() {
		Position p = getPosition();
		return new Position(p.getRegionLocalX(), p.getRegionLocalY());
	}
	
	/**
	 * Gets the packed position of this object in an integer.
	 * @return an position instance of this object.
	 */
	public abstract int getLocalPos();
	
	/**
	 * Returns whether the object is dynamic.
	 * @return returns if it's a dynamic implementation
	 */
	public abstract boolean isDynamic();
	
	/**
	 * The instance of this object.
	 * @return object's instance.
	 */
	public abstract int getInstance();
	
	/**
	 * Gets the region of this object.
	 * @return region of this object.
	 */
	public abstract Region getRegion();
	
	/**
	 * Setting a new position for this object.
	 */
	public abstract GameObject setPosition(Position position);
	
	/**
	 * Gets the object in the dynamic form.
	 * @return an {@link DynamicObject} instance.
	 */
	public abstract DynamicObject toDynamic();
	
	/**
	 * Gets the object in the dynamic form.
	 * @return an {@link DynamicObject} instance.
	 */
	public abstract StaticObject toStatic();
	
	/**
	 * Gets to know if the object is currently registered.
	 * @return <code>true</code> if it is, <code>false</code> otherwise.
	 */
	public boolean isReg() {
		Region region = getRegion();
		return region != null && region.getObject(getId(), getLocalPos()).isPresent();
	}
	
	public synchronized void publish() {
		Region r = getRegion();
		if(r != null) {
			r.addObj(this);
			clip(r);
		}
	}
	
	public synchronized void remove() {
		Region r = getRegion();
		if(r != null) {
			r.removeObj(this);
			unclip(r);
		}
	}
	
	/**
	 * Clips the object on the traversable map.
	 */
	public void clip(Region reg) {
		TraversalMap.markObject(reg, this, true, false);
		visible(true);
	}
	
	/**
	 * Unclips the object from the traversable map.
	 */
	public void unclip(Region reg) {
		TraversalMap.markObject(reg, this, false, false);
		visible(false);
	}
	
	/**
	 * The method that attempts to register this object and then execute
	 * {@code action} after specified amount of ticks.
	 * @param ticks the amount of ticks to unregister this object after.
	 */
	public void publish(int ticks, Consumer<GameObject> action) {
		publish();
		GameObject ref = this;
		World.get().submit(new Task(ticks, false) {
			@Override
			public void execute() {
				action.accept(ref);
				this.cancel();
			}
		});
	}
	
	/**
	 * Adds the object to the removable object backing set.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public boolean delete() {
		Region region = getRegion();
		return region != null && region.getRemovedObjects().add(this);
	}
	
	/**
	 * Removes the object from the removable object backing set.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public boolean restore() {
		Region region = getRegion();
		return region != null && region.getRemovedObjects().remove(this);
	}
	
	/**
	 * Gets the identification of this object.
	 * @return the identification.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the direction this object is facing.
	 * @return the direction.
	 */
	public ObjectDirection getDirection() {
		return direction;
	}
	
	/**
	 * Gets the type of object that this is.
	 * @return the type of the object.
	 */
	public ObjectType getObjectType() {
		return type;
	}
	
	/**
	 * Gets the definition of this object.
	 * @return the definition of the object.
	 */
	public ObjectDefinition getDefinition() {
		return ObjectDefinition.DEFINITIONS[id];
	}
	
	@Override
	public String toString() {
		return "OBJECT[id= " + id + ", type= " + getObjectType().toString() + ", type= " + getObjectType().toString() + ", dir= " + getDirection().toString() + ", instance= " + getInstance() + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getInstance(), getX(), getY(), getY(), getDirection(), getObjectType());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GameObject) {
			GameObject other = (GameObject) obj;
			return getId() == other.getId() && getInstance() == other.getInstance() && getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ() && getDirection() == other.getDirection() && getObjectType() == other.getObjectType();
		}
		return false;
	}
	
	/**
	 * Finds if this object is an anchor point for the door.
	 * @param obj clicked object.
	 * @return flag.
	 */
	public boolean isAdjacantDoor(GameObject obj) {
		int dx = getX() - obj.getX();
		int dy = getY() - obj.getY();
		if(dx >= 1 && direction == EAST) {
			return false;
		}
		if(dx <= -1 && direction == WEST) {
			return false;
		}
		if(dy <= -1 && direction == SOUTH) {
			return false;
		}
		if(dy >= 1 && direction == NORTH) {
			return false;
		}
		if(obj.getDirection() == NORTH && dy <= -1) {
			return false;
		}
		if(obj.getDirection() == SOUTH && dy >= 1) {
			return false;
		}
		if(obj.getDirection() == EAST && dx <= -1) {
			return false;
		}
		if(obj.getDirection() == WEST && dx >= 1) {
			return false;
		}
		if(dx == 0 && (obj.getDirection() == EAST || obj.getDirection() == WEST) && direction != obj.getDirection()) {
			return false;
		}
		if(dy == 0 && (obj.getDirection() == SOUTH || obj.getDirection() == NORTH) && direction != obj.getDirection()) {
			return false;
		}
		return true;
	}
}
