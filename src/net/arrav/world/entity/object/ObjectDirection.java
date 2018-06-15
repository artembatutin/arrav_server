package net.arrav.world.entity.object;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Optional;

/**
 * The enumerated type whose elements represent the directions for objects.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum ObjectDirection {
	/**
	 * The north orientation.
	 */
	NORTH(1, 2),
	
	/**
	 * The south orientation.
	 */
	SOUTH(3, 0),
	
	/**
	 * The east orientation.
	 */
	EAST(2, 3),
	
	/**
	 * The west orientation.
	 */
	WEST(0, 1);
	
	/**
	 * The identification of this direction.
	 */
	private final int id;
	
	/**
	 * The rotate identification of this direction.
	 */
	private final int rotate;
	
	/**
	 * Creates a new {@link ObjectDirection}.
	 */
	ObjectDirection(int id, int rotate) {
		this.id = id;
		this.rotate = rotate;
	}
	
	/**
	 * Gets the identification of this direction.
	 * @return the identification of this direction.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * A mutable {@link Int2ObjectOpenHashMap} of {@code int} keys to
	 * {@link ObjectDirection} values.
	 */
	private static final Int2ObjectOpenHashMap<ObjectDirection> values = new Int2ObjectOpenHashMap<>();
	
	/*
	 * Populates the {@link #values} cache.
	 */
	static {
		for(ObjectDirection orientation : values()) {
			values.put(orientation.getId(), orientation);
		}
	}
	
	/**
	 * Returns a {@link ObjectDirection} wrapped in an {@link Optional}
	 * for the specified {@code id}.
	 * @param id The game object orientation id.
	 * @return The optional game object orientation.
	 */
	public static Optional<ObjectDirection> valueOf(int id) {
		return Optional.ofNullable(values.get(id));
	}
	
	/**
	 * Gets the rotated direction.
	 * @return rotated direction.
	 */
	public ObjectDirection rotate() {
		return values.get(rotate);
	}
	
}