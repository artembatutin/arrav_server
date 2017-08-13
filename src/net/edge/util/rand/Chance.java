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
	 * Describing a {@code VERY_COMMON} chance (25%, 1 in 4 chance).
	 */
	VERY_COMMON(new Rational(1, 4)),
	
	/**
	 * Describing an {@code COMMON} chance (10%, 1 in 10 chance).
	 */
	COMMON(new Rational(1, 10)),
	
	/**
	 * Describing an {@code UNCOMMON} chance (5%, 1 in 20 chance).
	 */
	UNCOMMON(new Rational(1, 20)),
	
	/**
	 * Describing an {@code VERY_UNCOMMON} chance (4%, 2 in 50 chance).
	 * chance is now
	 */
	VERY_UNCOMMON(new Rational(2, 50)),
	
	/**
	 * Describing an {@code RARE} chance (2%, 1 in 50 chance).
	 */
	RARE(new Rational(1, 50)),
	
	/**
	 * Describing an {@code VERY_RARE} chance (1.428%, 1 in 70 chance).
	 */
	VERY_RARE(new Rational(1, 70)),
	
	/**
	 * Describing an {@code VERY_RARE} chance (0.8%, 1 in 125 chance).
	 */
	EXTREMELY_RARE(new Rational(1, 150)),
	FUCKING_RARE(new Rational(1, 300));

	private final Rational rational;
	
	Chance(Rational rational) {
		this.rational = rational;
	}
	
	public Rational getRational() {
		return rational;
	}
	
}
