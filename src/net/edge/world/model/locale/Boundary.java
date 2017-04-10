package net.edge.world.model.locale;

/**
 * Represents a single boundary check.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Boundary {
	
	/**
	 * The start left bottom corner of the boundary.
	 */
	private final Position start;
	
	/**
	 * The end right top corner of the boundary.
	 */
	private final Position end;
	
	/**
	 * Constructs a single square boundary check.
	 * @param pos  the start position.
	 * @param size the size of the entity.
	 */
	public Boundary(Position pos, int size) {
		start = pos;
		end = new Position(pos.getX() + size - 1, pos.getY() + size - 1);
	}
	
	/**
	 * Determines if the other {@link Position} with it's size is inside the boundary.
	 * @param other the other position.
	 * @param size  the other entity size.
	 * @return {@code true} the other entity is inside the boundary, {@code false} otherwise.
	 */
	public boolean inside(Position other, int size) {
		final Position otherEnd = new Position(other.getX() + size - 1, other.getY() + size - 1);
		return !(start.getX() - otherEnd.getX() > 0) && !(end.getX() - other.getX() < 0) && !(end.getY() - other.getY() < 0) && !(start.getY() - otherEnd.getY() > 0);
	}
	
	/**
	 * Determines if the other {@link Position} with it's size is within a distance the boundary.
	 * @param other the other position.
	 * @param size  the other entity size.
	 * @return {@code true} the other entity is within the boundary, {@code false} otherwise.
	 */
	public boolean within(Position other, int size, int distance) {
		final Position otherEnd = new Position(other.getX() + size - 1, other.getY() + size - 1);
		return !(start.getX() - otherEnd.getX() - distance > 0) && !(end.getX() - other.getX() + distance < 0) && !(end.getY() - other.getY() + distance < 0) && !(start.getY() - otherEnd.getY() - distance > 0);
	}
	
	public int getStartX() {
		return start.getX();
	}
	
	public int getStartY() {
		return start.getY();
	}
	
	public int getEndX() {
		return end.getX();
	}
	
	public int getEndY() {
		return end.getY();
	}
	
}
