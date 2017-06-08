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
	 * Describing an {@code UNCOMMON} chance (20%, 2 in 10 chance).
	 */
	UNCOMMON(new Rational(2, 10)),
	
	/**
	 * Describing an {@code VERY_UNCOMMON} chance (15%, 3 in 20 chance).
	 * chance is now
	 */
	VERY_UNCOMMON(new Rational(3, 20)),
	
	/**
	 * Describing an {@code RARE} chance (5%, 2 in 40 chance).
	 */
	RARE(new Rational(2, 40)),
	
	/**
	 * Describing an {@code VERY_RARE} chance (2.5%, 1 in 40 chance).
	 */
	VERY_RARE(new Rational(1, 40)),
	
	/**
	 * Describing an {@code VERY_RARE} chance (1%, 1 in 125 chance).
	 */
	EXTREMELY_RARE(new Rational(1, 100));
	
	private final Rational rational;
	
	Chance(Rational rational) {
		this.rational = rational;
	}
	
	public Rational getRational() {
		return rational;
	}
}
