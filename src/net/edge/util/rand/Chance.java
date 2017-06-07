package net.edge.util.rand;

/**
 * The enumerated type whose elements represent the chance rates.
 * @author lare96 <http://github.com/lare96>
 */
public enum Chance {
	
	/**
	 * Describing an {@code ALWAYS} chance (100%, 1 in 1 chance).
	 */
	ALWAYS(new Rational(1, 1)),
	
	/**
	 * Describing a {@code VERY_COMMON} chance (50%, 1 in 2 chance).
	 */
	VERY_COMMON(new Rational(1, 2)),
	
	/**
	 * Describing an {@code COMMON} chance (25%, 1 in 4 chance).
	 */
	COMMON(new Rational(1, 4)),
	
	/**
	 * Describing an {@code UNCOMMON} chance (10%, 1 in 10 chance).
	 */
	UNCOMMON(new Rational(1, 10)),
	
	/**
	 * Describing an {@code VERY_UNCOMMON} chance (2.5%, 5 in 200 chance).
	 */
	VERY_UNCOMMON(new Rational(5, 200)),
	
	/**
	 * Describing an {@code RARE} chance (0.7%, 1 in 150 chance).
	 */
	RARE(new Rational(1, 150)),
	
	/**
	 * Describing an {@code VERY_RARE} chance (0.3%, 1 in 300 chance).
	 */
	VERY_RARE(new Rational(1, 300)),
	
	/**
	 * Describing an {@code VERY_RARE} chance (0.2%, 1 in 500 chance).
	 */
	EXTREMELY_RARE(new Rational(1, 500));
	
	private final Rational rational;
	
	Chance(Rational rational) {
		this.rational = rational;
	}
	
	public Rational getRational() {
		return rational;
	}
}
