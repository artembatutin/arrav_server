package net.arrav.world.entity.object;

import net.arrav.world.World;
import net.arrav.world.entity.region.Region;
import net.arrav.world.locale.Position;

import java.util.Optional;

/**
 * An implementation of {@link GameObject} that is in a dynamic state.
 * @author Artem Batutin
 */
public class DynamicObject extends GameObject {
	
	/**
	 * The count of elements this object contains.
	 */
	private int elements;
	
	/**
	 * The condition whether the object is being disabled for interaction.
	 */
	private boolean disabled;
	
	/**
	 * The instance of this object.
	 */
	private final int instance;
	
	/**
	 * The final position of this object.
	 */
	private final Position position;
	
	/**
	 * Creating a {@link DynamicObject} with a {@link GameObject}.
	 * @param o object node converting.
	 */
	public DynamicObject(GameObject o) {
		this(o.getId(), new Position(o.getX(), o.getY(), o.getZ()), o.getDirection(), o.getObjectType(), false, 0, 0);
	}
	
	/**
	 * Creating a new {@link DynamicObject}
	 */
	public DynamicObject(int id, Position position, ObjectDirection direction, ObjectType type, boolean disabled, int elements, int instance) {
		super(id, direction, type);
		this.elements = elements;
		this.disabled = disabled;
		this.position = position;
		this.instance = instance;
	}
	
	@Override
	public GameObject copy() {
		return new DynamicObject(getId(), new Position(getX(), getY(), getZ()), getDirection(), getObjectType(), disabled, elements, instance);
	}
	
	@Override
	public GameObject copy(ObjectDirection direction) {
		return new DynamicObject(getId(), new Position(getX(), getY(), getZ()), direction, getObjectType(), disabled, elements, instance);
	}
	
	@Override
	public GameObject copy(ObjectType type) {
		return new DynamicObject(getId(), new Position(getX(), getY(), getZ()), getDirection(), type, disabled, elements, instance);
	}
	
	@Override
	public int getX() {
		return position.getX();
	}
	
	@Override
	public int getY() {
		return position.getY();
	}
	
	@Override
	public int getZ() {
		return position.getZ();
	}
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	@Override
	public int getLocalPos() {
		return position.toLocalPacked();
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public int getInstance() {
		return instance;
	}
	
	@Override
	public Region getRegion() {
		return World.getRegions().getRegion(position);
	}
	
	@Override
	public DynamicObject setPosition(Position position) {
		//Removing from the region as it has been changed.
		remove();
		return new DynamicObject(getId(), position, getDirection(), getObjectType(), disabled, elements, instance);
	}
	
	@Override
	public DynamicObject toDynamic() {
		return this;
	}
	
	@Override
	public StaticObject toStatic() {
		return new StaticObject(getRegion(), getId(), position.getLocalX(), position.getLocalY(), getZ(), getDirection(), getObjectType());
	}
	
	@Override
	public boolean isReg() {
		return !disabled && super.isReg();
	}
	
	/**
	 * @return the producing
	 */
	public int getElements() {
		return elements;
	}
	
	/**
	 * @param elements the elements count to set
	 */
	public void setElements(int elements) {
		this.elements = elements;
	}
	
	/**
	 * @return the disabled condition if the object is disabled for interaction.
	 */
	public boolean isDisabled() {
		return disabled;
	}
	
	/**
	 * @param disabled the condition whether or not the object is disabled.
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
