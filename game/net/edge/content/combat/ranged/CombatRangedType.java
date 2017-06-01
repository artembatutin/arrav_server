package net.edge.content.combat.ranged;

/**
 * The enumerated type whose elements represent a set of constants used to differ
 * between ranging weapons.
 * @author <a href="https://www.rune-server.org/members/yoshisaur//">Yoshisaur</a>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum CombatRangedType {
	SHORTBOW,
	COMPOSITE_BOW,
	LONGBOW,
	CROSSBOW,
	THROWNAXE,
	SPECIAL_BOW,
	JAVELIN,
	DART,
	KNIFE,
	CHINCHOMPA,
	HAND_CANNON,
	SALAMANDER;
	
	/**
	 * Determines if the underlying {@link #SPECIAL_BOW} requires ammunition.
	 * @return {@code true} if it does, {@code false} otherwise.
	 */
	public boolean isSpecialBow() {
		return this.equals(SPECIAL_BOW);
	}
	
	/**
	 * Determines if the bow should check if the player has the correct ammunition
	 * to use.
	 * @return {@code true} if it should check, {@code false} otherwise.
	 */
	public boolean checkAmmunition() {
		return this.equals(SHORTBOW) || this.equals(LONGBOW) || this.equals(CROSSBOW) || this.equals(HAND_CANNON) || this.equals(SALAMANDER);
	}
}
