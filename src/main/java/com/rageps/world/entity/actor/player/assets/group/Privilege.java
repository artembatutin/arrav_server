package com.rageps.world.entity.actor.player.assets.group;

/**
 * Represents a privilege a Player can have.
 *
 * @author Arithium
 */
public interface Privilege {

	/**
	 * The priority of the rank
	 *
	 * @return The priority of the rank
	 */
	int priority();

	/**
	 * The displayed Crown of this Privilege.
	 *
	 * @return The displayed Crown of this Privilege.
	 */
	Crown getCrown();

	/**
	 * Checks if this privilege is superior to {@link Privilege}
	 *
	 * @param privilege The {@link Privilege} to check if is superior too
	 * @return If this privilege is superior to the provided {@link Privilege}
	 */
	default boolean isSuperior(Privilege privilege) {
		return priority() > privilege.priority();
	}

	/**
	 * Checks if this privilege is inferior to {@link Privilege}
	 *
	 * @param privilege The {@link Privilege} to compare this privilege to.
	 * @return If this privilege is inferior to the provided {@link Privilege}
	 */
	default boolean isInferior(Privilege privilege) {
		return priority() < privilege.priority();
	}

	/**
	 * Compares this Privilege with the other specified Privilege.
	 *
	 * @param other The other privilege.
	 * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater than {@code 0} if {@code x > y}
	 */
	default int compare(Privilege other) {
		return Integer.compare(priority(), other.priority());
	}

}
