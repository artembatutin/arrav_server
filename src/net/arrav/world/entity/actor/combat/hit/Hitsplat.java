package net.arrav.world.entity.actor.combat.hit;

public enum Hitsplat {
	
	/**
	 * Represents a normal hit type.
	 */
	NORMAL(0),
	
	/**
	 * Represents a critical hit type.
	 */
	CRITICAL(1),
	
	/**
	 * Represents a poison hit type.
	 */
	POISON(2),
	
	/**
	 * Represents a disease hit type.
	 */
	DISEASE(3),
	
	/**
	 * Represents a heal hit type.
	 */
	HEAL(4),
	
	/**
	 * Represents a local normal hit type.
	 */
	NORMAL_LOCAL(5),
	
	/**
	 * Represents a local critical hit type.
	 */
	CRITICAL_LOCAL(6),
	
	/**
	 * Represents a local poison hit type.
	 */
	POISON_LOCAL(7),
	
	/**
	 * Represents a local disease hit type.
	 */
	DISEASE_LOCAL(8),
	
	/**
	 * Represents a local heal hit type.
	 */
	HEAL_LOCAL(9);
	
	/**
	 * The identification for this hit type.
	 */
	private final int id;
	
	/**
	 * Create a new {@link Hitsplat}.
	 * @param id the identification for this hit type.
	 */
	Hitsplat(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the identification for this hit type.
	 * @return the identification for this hit type.
	 */
	public final int getId() {
		return id;
	}
}