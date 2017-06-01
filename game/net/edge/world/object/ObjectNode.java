package net.edge.world.object;

import net.edge.task.Task;
import net.edge.World;
import net.edge.locale.Position;
import net.edge.world.region.Region;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * The node that represents an object anywhere in the world.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ObjectNode {
	
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
	 * Creates a new {@link ObjectNode}.
	 * @param id        the identification of the object.
	 * @param direction the direction this object is facing.
	 * @param type      the type of object that this is.
	 */
	public ObjectNode(int id, ObjectDirection direction, ObjectType type) {
		this.id = id;
		this.type = type;
		this.direction = direction;
	}
	
	/**
	 * Creates a new {@link ObjectNode} with the default {@code objectType}.
	 * @param id        the identification of the object.
	 * @param direction the direction this object is facing.
	 */
	public ObjectNode(int id, ObjectDirection direction) {
		this(id, direction, ObjectType.GENERAL_PROP);
	}
	
	public void visible(boolean on) {
		World.getRegions().getAllSurroundingRegions(getRegion().getRegionId()).forEach(r -> r.getPlayers().forEach((i, p) -> {
			if(getZ() == p.getPosition().getZ() && getInstance() == p.getInstance()) {
				if(on)
					p.getMessages().sendObject(this);
				else
					p.getMessages().sendRemoveObject(this);
			}
		}));
	}
	
	/**
	 * Gets this object with the new id.
	 * @param id new direction to set.
	 * @return the copy of this object.
	 */
	public ObjectNode setId(int id) {
		unregister();
		this.id = id;
		return this;
	}
	
	/**
	 * Gets a copy of this object with the new {@link ObjectDirection}.
	 * @param direction new direction to set.
	 * @return the copy of this object.
	 */
	public ObjectNode setDirection(ObjectDirection direction) {
		unregister();
		return copy(direction);
	}
	
	/**
	 * Gets a copy of this object with the new {@link ObjectType}.
	 * @param objectType new type to set.
	 * @return the copy of this object.
	 */
	public ObjectNode setObjectType(ObjectType objectType) {
		unregister();
		return copy(objectType);
	}
	
	/**
	 * Gets a copy of this object.
	 * @return the new object created.
	 */
	public abstract ObjectNode copy();
	
	/**
	 * Copying the object with a new specified {@link ObjectDirection}.
	 * @param direction the specified direction.
	 * @return the new object created.
	 */
	public abstract ObjectNode copy(ObjectDirection direction);
	
	/**
	 * Copying the object with a new specified {@link ObjectType}.
	 * @param type the specified type.
	 * @return the new object created.
	 */
	public abstract ObjectNode copy(ObjectType type);
	
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
	public abstract Position getGlobalPos();
	
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
	 * @param position
	 */
	public abstract ObjectNode setPosition(Position position);
	
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
		return getRegion().getObject(getId(), getLocalPos()).isPresent();
	}
	
	/**
	 * Registers the object in the region it is in.
	 * @return <code>true</code> if succeeded, <code>false</code> otherwise
	 */
	public boolean register() {
		if(getRegion().addObj(this)) {
			World.getTraversalMap().markObject(getRegion(), this, true, false);
			visible(true);
			return true;
		}
		return false;
	}
	
	/**
	 * Unregisters the object from the region it is in.
	 * @return <code>true</code> if succeeded, <code>false</code> otherwise
	 */
	public boolean unregister() {
		if(getRegion().removeObj(this)) {
			World.getTraversalMap().markObject(getRegion(), this, false, false);
			visible(false);
			return true;
		}
		return false;
	}
	
	/**
	 * The method that attempts to register this object and then execute
	 * {@code action} after specified amount of ticks.
	 * @param ticks  the amount of ticks to unregister this object after.
	 * @return {@code true} if the object was registered, {@code false}
	 * otherwise.
	 */
	public boolean register(int ticks, Consumer<ObjectNode> action) {
		if(register()) {
			ObjectNode ref = this;
			World.submit(new Task(ticks, false) {
				@Override
				public void execute() {
					action.accept(ref);
					this.cancel();
				}
			});
			return true;
		}
		return false;
	}
	
	/**
	 * Adds the object to the removable object backing set.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public boolean delete() {
		return getRegion().getRemovedObjects(getLocalPos()).add(this);
	}
	
	/**
	 * Removes the object from the removable object backing set.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public boolean restore() {
		return getRegion().getRemovedObjects(getLocalPos()).remove(this);
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
		if(obj instanceof ObjectNode) {
			ObjectNode other = (ObjectNode) obj;
			return getId() == other.getId() && getInstance() == other.getInstance() && getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ() && getDirection() == other.getDirection() && getObjectType() == other.getObjectType();
		}
		return false;
	}

}
