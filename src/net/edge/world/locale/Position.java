package net.edge.world.locale;

import net.edge.util.rand.RandomUtils;
import net.edge.world.Direction;

/**
 * The container class that represents a coordinate anywhere in the world.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public class Position {
	
	/**
	 * The {@code X} coordinate.
	 */
	private final int x;
	
	/**
	 * The {@code Y} coordinate.
	 */
	private final int y;
	
	/**
	 * The {@code Z} coordinate.
	 */
	private final int z;
	
	/**
	 * Creates a new {@link Position}.
	 * @param x the {@code X} coordinate.
	 * @param y the {@code Y} coordinate.
	 * @param z the {@code Z} coordinate.
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new {@link Position} with the {@code Z} coordinate value as
	 * {@code 0}.
	 * @param x the {@code X} coordinate.
	 * @param y the {@code Y} coordinate.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}
	
	/**
	 * Increments the {@code X}, {@code Y}, and {@code Z} coordinate values
	 * within this container by {@code amountX}, {@code amountY}, and
	 * {@code amountZ}.
	 * @param position the position to gather the amount to increment the coordinate by.
	 * @return an instance of this position.
	 */
	public final Position move(Position position) {
		int x = (this.x + position.getX());
		int y = (this.y + position.getY());
		int z = (this.z + position.getZ());
		return new Position(x, y, z);
	}
	
	/**
	 * Increments the {@code X} and {@code Y} coordinate values within this
	 * container by deltas of the set {@code Direction}.
	 * @param direction the direction to move.
	 * @return an instance of this position.
	 */
	public final Position move(Direction direction) {
		return move(new Position(direction.getX(), direction.getY(), 0));
	}
	
	/**
	 * Increments the {@code X}, {@code Y}, and {@code Z} coordinate values
	 * within this container by {@code amountX}, {@code amountY}, and
	 * {@code amountZ}.
	 * @param amountX the amount to increment the {@code X} coordinate by.
	 * @param amountY the amount to increment the {@code Y} coordinate by.
	 * @param amountZ the {@code Z} coordinate to be set.
	 * @return an instance of this position.
	 */
	public final Position move(int amountX, int amountY, int amountZ) {
		return this.move(new Position(amountX, amountY, amountZ));
	}
	
	/**
	 * Increments the {@code X} and {@code Y} coordinate values within this
	 * container by {@code amountX} and {@code amountY}.
	 * @param amountX the amount to increment the {@code X} coordinate by.
	 * @param amountY the amount to increment the {@code Y} coordinate by.
	 * @return an instance of this position.
	 */
	public final Position move(int amountX, int amountY) {
		return move(new Position(amountX, amountY));
	}
	
	/**
	 * Increments the {@code X} and {@code Y} coordinate values within this
	 * container by random amounts positive and negative of {@code amount}.
	 * @return an instance of this position.
	 */
	public final Position random(int amount) {
		int x = RandomUtils.inclusive(amount);
		int y = RandomUtils.inclusive(amount);
		switch(RandomUtils.inclusive(3)) {
			case 1:
				return move(-x, -y);
			case 2:
				return move(-x, y);
			case 3:
				return move(x, -y);
			default:
				return move(x, y);
		}
	}
	
	/**
	 * Returns the delta coordinates. Note that the returned position is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * @param a the first position.
	 * @param b the second position.
	 * @return the delta coordinates contained within a position.
	 */
	public static Position delta(Position a, Position b) {
		return new Position(b.x - a.x, b.y - a.y);
	}
	
	/**
	 * Can access this method statically to register a new {@link Position}.
	 * @param pos The position to register.
	 * @return The created position.
	 */
	public static Position create(Position pos) {
		return new Position(pos.x, pos.y);
	}
	
	/**
	 * Can access this method statically to register a new {@link Position}.
	 * @param x The x-value of this position.
	 * @param y The y-value of this position.
	 * @return The created position.
	 */
	public static Position create(int x, int y) {
		return new Position(x, y);
	}
	
	/**
	 * Can access this method statically to register a new {@link Position}.
	 * @param x The x-value of this position.
	 * @param y The y-value of this position.
	 * @param z The z-value of this position.
	 * @return The created position.
	 */
	public static Position create(int x, int y, int z) {
		return new Position(x, y, z);
	}
	
	/**
	 * Returns a local packed integer.
	 * @return packed position integer.
	 */
	public int toLocalPacked() {
		return (z & 0x3f) << 12 | (getRegionLocalX() & 0x3f) << 6 | (getRegionLocalY() & 0x3f);
	}
	
	/**
	 * Gets the local packed X coordinate.
	 * @return regional local X.
	 */
	public int getRegionLocalX() {
		return x - (x & 0x3F) * 64;
	}
	
	/**
	 * Gets the local packed Y coordinate.
	 * @return regional local Y.
	 */
	public int getRegionLocalY() {
		return y - (y & 0x3F) * 64;
	}
	
	/**
	 * Gets the {@code X} coordinate.
	 * @return the {@code X} coordinate.
	 */
	public final int getX() {
		return x;
	}
	
	/**
	 * Gets the {@code Y} coordinate.
	 * @return the {@code Y} coordinate.
	 */
	public final int getY() {
		return y;
	}
	
	/**
	 * Gets the {@code Z} coordinate.
	 * @return the {@code Z} coordinate.
	 */
	public final int getZ() {
		return z;
	}
	
	/**
	 * Gets the region identification relative to this position.
	 * @return the region identification.
	 */
	public final int getRegion() {
		return ((getChunkX() << 8) + getChunkY());
	}
	
	/**
	 * Gets the {@code X} coordinate of the region containing this position.
	 * @return the {@code X} coordinate of the region.
	 */
	public final int getRegionX() {
		return (x >> 3) - 6;
	}
	
	/**
	 * Gets the {@code Y} coordinate of the region containing this position.
	 * @return the {@code Y} coordinate of the region
	 */
	public final int getRegionY() {
		return (y >> 3) - 6;
	}
	
	/**
	 * Gets the local {@code X} coordinate relative to {@code base}.
	 * @param base the relative base position.
	 * @return the local {@code X} coordinate.
	 */
	public final int getLocalX(Position base) {
		return x - 8 * base.getRegionX();
	}
	
	/**
	 * Gets the local {@code Y} coordinate relative to {@code base}.
	 * @param base the relative base position.
	 * @return the local {@code Y} coordinate.
	 */
	public final int getLocalY(Position base) {
		return y - 8 * base.getRegionY();
	}
	
	/**
	 * Gets the local {@code X} coordinate relative to this position.
	 * @return the local {@code X} coordinate.
	 */
	public final int getLocalX() {
		return getLocalX(this);
	}
	
	/**
	 * Gets the local {@code Y} coordinate relative to this Position.
	 * @return the local {@code Y} coordinate.
	 */
	public final int getLocalY() {
		return getLocalY(this);
	}
	
	/**
	 * Gets the {@code X} region chunk relative to this position.
	 * @return the {@code X} region chunk.
	 */
	private int getChunkX() {
		return (x >> 6);
	}
	
	/**
	 * Gets the {@code Y} region chunk relative to this position.
	 * @return the {@code Y} region chunk.
	 */
	private int getChunkY() {
		return (y >> 6);
	}
	
	/**
	 * Returns the base local x coordinate.
	 * @return The base local x coordinate.
	 */
	public int getBaseLocalX() {
		return getTopLeftRegionX() * 8;
	}
	
	/**
	 * Returns the base local y coordinate.
	 * @return The base local y coordinate.
	 */
	public int getBaseLocalY() {
		return getTopLeftRegionY() * 8;
	}
	
	/**
	 * Gets the x coordinate of the region.
	 * @return The region x coordinate.
	 */
	public int getTopLeftRegionX() {
		return x / 8 - 6;
	}
	
	/**
	 * Gets the y coordinate of the region.
	 * @return The region y coordinate.
	 */
	public int getTopLeftRegionY() {
		return y / 8 - 6;
	}
	
	/**
	 * Gets the absolute x coordinate of this Region (which can be compared directly against {@link Position#getX()}.
	 * @return The absolute x coordinate.
	 */
	public int getAbsoluteX() {
		return 8 * (x + 6);
	}
	
	/**
	 * Gets the absolute y coordinate of this Region (which can be compared directly against {@link Position#getY()}.
	 * @return The absolute y coordinate.
	 */
	public int getAbsoluteY() {
		return 8 * (y + 6);
	}
	
	/**
	 * Calculates if the position is within a rectangle.
	 * @param x1 the x coordinate of the top left corner.
	 * @param y1 the y coordinate of the top left corner
	 * @param x2 the x coordinate of the bottom right corner.
	 * @param y2 the y coordinate of the bottom right corner.
	 * @param z  the z coordinate of the height.
	 * @return {@code true} if the coordinate is within this rectangle.
	 */
	public boolean within(int x1, int y1, int x2, int y2, int z) {
		return (this.getX() > x1 && this.getY() < y1) && (this.getX() < x2 && this.getY() > y2 && this.getZ() == z);
	}
	
	/**
	 * Calculates if the position is within a rectangle.
	 * @param x1 the x coordinate of the top left corner.
	 * @param y1 the y coordinate of the top left corner
	 * @param x2 the x coordinate of the bottom right corner.
	 * @param y2 the y coordinate of the bottom right corner.
	 * @return {@code true} if the coordinate is within this rectangle.
	 */
	public boolean within(int x1, int y1, int x2, int y2) {
		return within(x1, y1, x2, y2, z);
	}
	
	/**
	 * Determines if this position is within {@code amount} distance of
	 * {@code other}.
	 * @param other  the position to check the distance for.
	 * @param amount the distance to check.
	 * @return {@code true} if this position is within the distance,
	 * {@code false} otherwise.
	 */
	public final boolean withinDistance(Position other, int amount) {
		return this.z == other.z && Math.abs(other.x - this.x) <= amount && Math.abs(other.y - this.y) <= amount;
	}
	
	/**
	 * Returns the distance between this and the {@code other} position.
	 * @param other the other position.
	 * @return The amount of distance between this and the other position.
	 */
	public final double getDistance(Position other) {
		int x = this.x - other.x;
		int y = this.y - other.y;
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Gets the longest horizontal or vertical delta between the two positions.
	 * @param other The other position.
	 * @return The longest horizontal or vertical delta.
	 */
	public int getLongestDelta(Position other) {
		int deltaX = Math.abs(getX() - other.getX());
		int deltaY = Math.abs(getY() - other.getY());
		return Math.max(deltaX, deltaY);
	}
	
	/**
	 * Determines if this position is viewable from {@code other}.
	 * @param other the other position to determine if viewable from.
	 * @return {@code true} if this position is viewable, {@code false}
	 * otherwise.
	 */
	public final boolean isViewableFrom(Position other) {
		if(this.getZ() != other.getZ())
			return false;
		Position p = Position.delta(this, other);
		return p.x <= 14 && p.x >= -15 && p.y <= 14 && p.y >= -15;
	}
	
	/**
	 * Creates a backing copy of this position.
	 */
	public Position copy() {
		return new Position(x, y, z);
	}
	
	public final boolean same(Position other) {
		return other != null && x == other.x && y == other.y && z == other.z;
	}
	
	@Override
	public String toString() {
		return "POSITION[x= " + x + ", y= " + y + ", z= " + z + "]";
	}
	
	@Override
	public final boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Position))
			return false;
		Position other = (Position) obj;
		return x == other.x && y == other.y && z == other.z;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}
}