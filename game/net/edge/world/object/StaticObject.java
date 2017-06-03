package net.edge.world.object;

import net.edge.world.World;
import net.edge.locale.Position;
import net.edge.world.node.region.Region;

/**
 * An implementation of {@link ObjectNode} that is in a static state.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class StaticObject extends ObjectNode {
	
	/**
	 * A reference to the region containing this static object.
	 */
	private final Region region;
	
	/**
	 * The packed coordinates (local XY and height) for this object.
	 */
	private final int packedCoordinates;
	
	public StaticObject(Region region, int id, int x, int y, int height, ObjectDirection direction, ObjectType type) {
		super(id, direction, type);
		this.region = region;
		this.packedCoordinates = (height & 0x3f) << 12 | (x & 0x3f) << 6 | (y & 0x3f);
	}
	
	public StaticObject(Region region, int id, int packedCoordinates, ObjectDirection direction, ObjectType type) {
		super(id, direction, type);
		this.region = region;
		this.packedCoordinates = packedCoordinates;
	}
	
	@Override
	public ObjectNode copy() {
		return new StaticObject(region, getId(), packedCoordinates, getDirection(), getObjectType());
	}
	
	@Override
	public ObjectNode copy(ObjectDirection direction) {
		return new StaticObject(region, getId(), packedCoordinates, direction, getObjectType());
	}
	
	@Override
	public ObjectNode copy(ObjectType type) {
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
	public Position getGlobalPos() {
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
		Region reg = World.getRegions().getRegion(pos);
		return new StaticObject(reg, getId(), pos.getX(), pos.getY(), pos.getZ(), getDirection(), getObjectType());
	}
	
	@Override
	public DynamicObject toDynamic() {
		return new DynamicObject(getId(), new Position(getX(), getY(), getZ()), getDirection(), getObjectType(), false, 0, 0);
	}
	
	@Override
	public StaticObject toStatic() {
		return this;
	}
}
