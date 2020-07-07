package com.rageps.world.locale.loc;

import com.rageps.util.rand.RandomUtils;
import com.rageps.world.locale.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * The location type that models any area in a square or rectangle shape.
 * @author lare96 <http://github.com/lare96>
 */
public final class SquareArea extends Area {
	
	/**
	 * The south-west {@code X} corner of the box.
	 */
	private final int swX;
	
	/**
	 * The south-west {@code Y} corner of the box.
	 */
	private final int swY;
	
	/**
	 * The north-east {@code X} corner of the box.
	 */
	private final int neX;
	
	/**
	 * The north-east {@code Y} corner of the box.
	 */
	private final int neY;
	
	/**
	 * The {@code Z} level of the box.
	 */
	private final int z;


	/**
	 * Creates a new {@link SquareArea} from a region id.
	 * @param regionId the region for which this area is being made for.
	 */
	public SquareArea(int regionId) {
		int baseX = ((regionId >> 8) & 0xFF) << 6;
		int baseY = (regionId & 0xFF) << 6;
		int size = 64 - 1;
		this.swX = baseX;
		this.swY = baseY;
		this.neX = baseX + size;
		this.neY = baseY + size;
		this.z = 0;
	}

	
	/**
	 * Creates a new {@link SquareArea}.
	 * @param swX the south-west {@code X} corner of the box.
	 * @param swY the south-west {@code Y} corner of the box.
	 * @param neX the north-east {@code X} corner of the box.
	 * @param neY the north-east {@code Y} corner of the box.
	 * @param z the {@code Z} level of the box.
	 */
	public SquareArea(int swX, int swY, int neX, int neY, int z) {
		this.swX = swX;
		this.swY = swY;
		this.neX = neX;
		this.neY = neY;
		this.z = z;
	}
	
	/**
	 * Creates a new {@link SquareArea} from the center position and radius.
	 * @param x the center {@code X} coordinate.
	 * @param y the center {@code Y} coordinate.
	 * @param z the center {@code Z} coordinate.
	 * @param radius the radius of this area from the center coordinates.
	 */
	public SquareArea(int x, int y, int z, int radius) {
		this(x - radius, y - radius, x + radius, y + radius, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(!(obj instanceof SquareArea))
			return false;
		SquareArea other = (SquareArea) obj;
		return other.swX == swX && other.swY == swY && other.neX == neX && other.neY == neY && other.z == z;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + neX;
		result = prime * result + neY;
		result = prime * result + swX;
		result = prime * result + swY;
		result = prime * result + z;
		return result;
	}
	
	@Override
	public boolean inArea(Position position) {
		if(position.getZ() != z)
			return false;
		return position.getX() >= swX && position.getX() <= neX && position.getY() >= swY && position.getY() <= neY;
	}
	
	@Override
	public boolean isWithin(Position position, int distance) {
		if(position.getZ() != z)
			return false;
		return position.getX() >= swX - distance && position.getX() <= neX + distance && position.getY() >= swY - distance && position.getY() <= neY + distance;
	}
	
	@Override
	public String toString() {
		return "SQUARE_LOCATION[swX= " + swX + ", swY= " + swY + ", neX= " + neX + ", neY= " + neY + "]";
	}
	
	@Override
	public Position random() {
		int x = RandomUtils.inclusive(Math.max(swX, neX) - Math.min(swX, neX) + 1) + Math.min(swX, neX);
		int y = RandomUtils.inclusive(Math.max(swY, neY) - Math.min(swY, neY) + 1) + Math.min(swY, neY);
		return new Position(x, y, z);
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return the copy of this instance that does not hold any references.
	 */
	public SquareArea copy() {
		return new SquareArea(swX, swY, neX, neY, z);
	}
	
	/**
	 * Gets the south-west {@code X} corner of the box.
	 * @return the {@code X} corner of the box.
	 */
	public int getSwX() {
		return swX;
	}
	
	/**
	 * Gets the south-west {@code Y} corner of the box.
	 * @return the {@code Y} corner of the box.
	 */
	public int getSwY() {
		return swY;
	}
	
	/**
	 * Gets the north-east {@code X} corner of the box.
	 * @return the {@code X} corner of the box.
	 */
	public int getNeX() {
		return neX;
	}
	
	/**
	 * Gets the north-east {@code Y} corner of the box.
	 * @return the {@code Y} corner of the box.
	 */
	public int getNeY() {
		return neY;
	}
	
	/**
	 * Gets the {@code Z} level of the box.
	 * @return the {@code Z} level of the box.
	 */
	public int getZ() {
		return z;
	}

	public SquareArea grow(Position position, int grow) {
		int north = position.getY() + grow;
		int east = position.getX() + grow;
		int west = position.getX() - grow;
		int south = position.getY() - grow;
		return new SquareArea(west, south, east, north, position.getZ());
	}

	public SquareArea grow(Position position, int x, int y, boolean fromCenter) {
		int north = position.getY() + y;
		int east = position.getX() + x;
		int west = fromCenter ? position.getX() - x : position.getX();
		int south = fromCenter ? position.getY() - y : position.getY();
		return new SquareArea(west, south, east, north, position.getZ());
	}

	public SquareArea of(Position p1, Position p2) {
		int north = Math.max(p1.getY(), p2.getY());
		int east = Math.max(p1.getX(), p2.getX());
		int south = Math.min(p1.getY(), p2.getY());
		int west = Math.min(p1.getX(), p2.getX());
		return new SquareArea(west, south, east, north);
	}
	/**
	 * Gets the ids of all the regions inside this {@link SquareArea}.
	 * @return The region ids.
	 */
	public List<Integer> getRegionIds() {
		List<Integer> regionIds = new ArrayList<>();
		for (int x = swX >> 6; x < (neX >> 6) + 1; x++) {
			for (int y = swY >> 6; y < (neY >> 6) + 1; y++) {
				int id = y | x << 8;
				regionIds.add(id);
			}
		}
		return regionIds;
	}
}
