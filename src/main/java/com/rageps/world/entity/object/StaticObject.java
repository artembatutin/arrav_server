package com.rageps.world.entity.object;

import com.rageps.world.World;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;

/**
 * An implementation of {@link GameObject} that is in a static state.
 * @author Artem Batutin
 */
public class StaticObject extends GameObject {
	
	/**
	 * A reference to the region containing this static object.
	 */
	private final Region region;
	
	/**
	 * The packed coordinates (local X Y and height) for this object.
	 */
	private final int packedCoordinates;
	
	public StaticObject(Region region, int id, int x, int y, int height, ObjectDirection direction, ObjectType type) {
		super(id, direction, type);
		this.packedCoordinates = (height & 0x3f) << 12 | (x & 0x3f) << 6 | (y & 0x3f);
		if(region == null)
			this.region = World.getRegions().getRegion(getRegionPos());
		else
			this.region = region;
	}
	
	public StaticObject(Region region, int id, int packedCoordinates, ObjectDirection direction, ObjectType type) {
		super(id, direction, type);
		this.packedCoordinates = packedCoordinates;
		if(region == null)
			this.region = World.getRegions().getRegion(getRegionPos());
		else
			this.region = region;
	}
	
	@Override
	public StaticObject copy() {
		return new StaticObject(region, getId(), packedCoordinates, getDirection(), getObjectType());
	}
	
	@Override
	public StaticObject copy(ObjectDirection direction) {
		return new StaticObject(region, getId(), packedCoordinates, direction, getObjectType());
	}
	
	@Override
	public StaticObject copy(ObjectType type) {
		return new StaticObject(region, getId(), packedCoordinates, getDirection(), type);
	}
	
	@Override
	public int getX() {
		return (packedCoordinates >> 6 & 0x3F) + region.getPosition().getX();
	}
	
	@Override
	public int getY() {
		return (packedCoordinates & 0x3F) + region.getPosition().getY();
	}
	
	@Override
	public int getZ() {
		return packedCoordinates >> 12 & 0x3;
	}
	
	@Override
	public Position getPosition() {
		return new Position(getX(), getY(), getZ());
	}
	
	@Override
	public int getLocalPos() {
		return packedCoordinates;
	}
	
	@Override
	public boolean isDynamic() {
		return false;
	}
	
	@Override
	public int getInstance() {
		return 0;
	}
	
	@Override
	public Region getRegion() {
		return region;
	}
	
	@Override
	public StaticObject setPosition(Position pos) {
		//Removing from the region as it has been changed.
		remove();
		return new StaticObject(World.getRegions().getRegion(pos), getId(), pos.getX(), pos.getY(), pos.getZ(), getDirection(), getObjectType());
	}
	
	@Override
	public DynamicObject toDynamic() {
		return new DynamicObject(getId(), new Position(getX(), getY(), getZ()), getDirection(), getObjectType(), false, 0, 0);
	}
	
	@Override
	public StaticObject toStatic() {
		return this;
	}
	
	/**
	 * Returns a new static object with a rotated direction.
	 * @return static object.
	 */
	public StaticObject rotate() {
		return new StaticObject(region, getId(), getLocalPos(), getDirection().rotate(), getObjectType());
	}
	
	/**
	 * Returns a new static object with a different type.
	 * @return static object.
	 */
	public StaticObject toggleType() {
		return new StaticObject(region, getId(), getLocalPos(), getDirection(), getObjectType().toggle());
	}
}
