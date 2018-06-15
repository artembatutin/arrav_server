package net.arrav.world.entity.actor.combat.attack;

/**
 * The enumerated type whose elements represent the fighting styles.
 * @author Michael | Chex
 */
public enum FightStyle {
	ACCURATE(3, 0, 0), AGGRESSIVE(0, 3, 0), DEFENSIVE(0, 0, 3), CONTROLLED(1, 1, 1);

	/**
	 * The increase to defense.
	 */
	private int accuracyIncrease;

	/**
	 * The increase to defense.
	 */
	private int defensiveIncrease;

	/**
	 * The increase to strength.
	 */
	private int aggressiveIncrease;

	/**
	 * Constructs a new {@link FightStyle} element.
	 * @param aggressiveIncrease the aggreaaive increase amount
	 * @param defensiveIncrease the defensive increase amount
	 */
	FightStyle(int accuracyIncrease, int aggressiveIncrease, int defensiveIncrease) {
		this.accuracyIncrease = accuracyIncrease;
		this.aggressiveIncrease = aggressiveIncrease;
		this.defensiveIncrease = defensiveIncrease;
	}

	/**
	 * Gets the accuracy increase for this attack type.
	 * @return the accuracy increase
	 */
	public int getAccuracyIncrease() {
		return accuracyIncrease;
	}

	/**
	 * Gets the aggressive increase for this attack type.
	 * @return the aggressive increase
	 */
	public int getAggressiveIncrease() {
		return aggressiveIncrease;
	}

	/**
	 * Gets the defense increase for this attack type.
	 * @return the defense increase
	 */
	public int getDefensiveIncrease() {
		return defensiveIncrease;
	}

}