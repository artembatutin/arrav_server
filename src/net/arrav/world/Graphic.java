package net.arrav.world;

/**
 * The container class that represents an graphic.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin
 */
public final class Graphic {
	
	/**
	 * The identification for this graphic.
	 */
	private final int id;
	
	/**
	 * The height of this graphic.
	 */
	private final int height;
	
	/**
	 * The delay of this graphic.
	 */
	private final int delay;
	
	/**
	 * Creates a new {@link Graphic}.
	 * @param id the identification for this graphic.
	 * @param height the height of this graphic.
	 * @param delay the delay of this graphic.
	 */
	public Graphic(int id, int height, int delay) {
		this.id = id;
		this.height = height;
		this.delay = delay;
	}
	
	/**
	 * Creates a new {@link Graphic} with a {@code delay} of {@code 0}.
	 * @param id the identification for this graphic.
	 * @param height the height for this graphic.
	 */
	public Graphic(int id, int height) {
		this(id, height, 0);
	}
	
	/**
	 * Creates a new {@link Graphic} with a {@code height} and {@code delay} of {@code 0}.
	 * @param id the identification for this graphic.
	 */
	public Graphic(int id) {
		this(id, 0, 0);
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy is <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return a reference-free copy of this instance.
	 */
	public Graphic copy() {
		return new Graphic(id, height, delay);
	}
	
	/**
	 * Gets the identification for this graphic.
	 * @return the identification for this graphic.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the height of this graphic.
	 * @return the height of this graphic.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the delay of this graphic.
	 * @return the delay of this graphic.
	 */
	public int getDelay() {
		return delay;
	}
}