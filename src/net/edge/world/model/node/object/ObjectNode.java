package net.edge.world.model.node.object;

import net.edge.world.model.node.NodeType;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.Node;

import java.util.Objects;

/**
 * The node that represents an object anywhere in the world.
 * @author lare96 <http://github.com/lare96>
 */
public class ObjectNode extends Node {
	
	/**
	 * The identification of this object.
	 */
	private int id;
	
	/**
	 * Determines if this object is disabled.
	 */
	private boolean disabled;
	
	/**
	 * The count of items this object can produce.
	 */
	private int producing;
	
	/**
	 * The direction this object is facing.
	 */
	private ObjectDirection direction;
	
	/**
	 * The type of object that this is.
	 */
	private ObjectType objectType;
	
	/**
	 * Creates a new {@link ObjectNode}.
	 * @param id        the identification of the object.
	 * @param position  the position of this object in the world.
	 * @param direction the direction this object is facing.
	 * @param type      the type of object that this is.
	 */
	public ObjectNode(int id, Position position, ObjectDirection direction, ObjectType type) {
		super(position, NodeType.OBJECT);
		this.id = id;
		this.direction = Objects.requireNonNull(direction);
		this.objectType = Objects.requireNonNull(type);
	}
	
	/**
	 * Creates a new {@link ObjectNode} with the default {@code objectType}.
	 * @param id        the identification of the object.
	 * @param position  the position of this object in the world.
	 * @param direction the direction this object is facing.
	 */
	public ObjectNode(int id, Position position, ObjectDirection direction) {
		this(id, position, direction, ObjectType.GENERAL_PROP);
	}
	
	@Override
	public void register() {
		World.getRegions().getAllSurroundingRegions(getPosition().getRegion()).forEach(r -> r.getPlayers().forEach((i, p) -> {
			if(super.getPosition().getZ() == p.getPosition().getZ() && super.getInstance() == p.getInstance())
				p.getMessages().sendObject(this);
		}));
	}
	
	@Override
	public void dispose() {
		World.getRegions().getAllSurroundingRegions(getPosition().getRegion()).forEach(r -> r.getPlayers().forEach((i, p) -> {
			if(super.getPosition().getZ() == p.getPosition().getZ() && super.getInstance() == p.getInstance())
				p.getMessages().sendRemoveObject(this);
		}));
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return the copy of this instance that does not hold any references.
	 */
	public ObjectNode copy() {
		return new ObjectNode(id, super.getPosition(), direction, objectType);
	}
	
	/**
	 * Gets the identification of this object.
	 * @return the identification.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets a new value to the {@link #id}.
	 * @param id the new value to set.
	 */
	public void setId(int id) {
		//Removing from the region as it has been changed.
		getRegion().unregister(this);
		this.id = id;
	}
	
	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}
	
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	/**
	 * @return the producing
	 */
	public int getProducingCount() {
		return producing;
	}
	
	/**
	 * @param producing the producing to set
	 */
	public void setProducingCount(int producing) {
		this.producing = producing;
	}
	
	/**
	 * Gets the direction this object is facing.
	 * @return the direction.
	 */
	public ObjectDirection getDirection() {
		return direction;
	}
	
	/**
	 * Sets a new value to the {@link #direction}.
	 * @param direction the new value to set.
	 */
	public void setDirection(ObjectDirection direction) {
		//Removing from the region as it has been changed.
		getRegion().unregister(this);
		this.direction = direction;
	}
	
	/**
	 * Gets the type of object that this is.
	 * @return the type of the object.
	 */
	public ObjectType getObjectType() {
		return objectType;
	}
	
	/**
	 * Sets a new value to the {@link #objectType}.
	 * @param objectType the new value to set.
	 */
	public void setObjectType(ObjectType objectType) {
		//Removing from the region as it has been changed.
		getRegion().unregister(this);
		this.objectType = objectType;
	}
	
	@Override
	public void setPosition(Position position) {
		//Removing from the region as it has been changed.
		if(getPosition() != null)
			getRegion().unregister(this);
		super.setPosition(position);
	}
	
	@Override
	public void setInstance(int instance) {
		//Removing from the region as it has been changed.
		getRegion().unregister(this);
		super.setInstance(instance);
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
		return "OBJECT[id= " + id + ", type= " + objectType.toString() + ", dir= " + direction.toString() + ", state= " + getState().toString().toLowerCase() + ", instance= " + getInstance() + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getInstance(), super.getPosition(), getDirection(), getObjectType());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectNode) {
			ObjectNode other = (ObjectNode) obj;
			return getId() == other.getId() && getInstance() == other.getInstance() && super.getPosition().equals(other.getPosition()) && getDirection() == other.getDirection() && getObjectType() == other.getObjectType();
		}
		return false;
	}
	
	//Region editor methods.
	
	/**
	 * Rotating the object. Used in the regional editor.
	 */
	public void rotate() {
		this.direction = direction.rotate();
	}
	
	public void toggleType() {
		this.objectType = objectType.toggle();
	}
	
	/**
	 * Creates a new {@link ObjectNode} with a new id.
	 * @param id the new id to be set.
	 * @return our new changed object.
	 */
	public ObjectNode createWithId(int id) {
		return new ObjectNode(id, getPosition(), getDirection(), getObjectType());
	}
	
	/**
	 * Creates a new {@link ObjectNode} with a new type.
	 * @param type the new id to be set.
	 * @return our new changed object.
	 */
	public ObjectNode createWithType(ObjectType type) {
		return new ObjectNode(id, getPosition(), getDirection(), type);
	}
	
}
