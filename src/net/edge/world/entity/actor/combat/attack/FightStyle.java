package net.edge.world.entity.actor.combat.attack;

/**
 * The enumerated type whose elements represent the fighting styles.
 *
 * @author Michael | Chex
 */
public enum FightStyle {
    ACCURATE(0, 0),
    AGGRESSIVE(3, 0),
    DEFENSIVE(0, 3),
    CONTROLLED(1, 1);

    /** The increase to defense. */
    private int defensiveIncrease;

    /** The increase to strength. */
    private int aggressiveIncrease;

    /**
     * Constructs a new {@link FightStyle} element.
     * @param aggressiveIncrease the aggreaaive increase amount
     * @param defensiveIncrease  the defensive increase amount
     */
    FightStyle(int aggressiveIncrease, int defensiveIncrease) {
        this.aggressiveIncrease = aggressiveIncrease;
        this.defensiveIncrease = defensiveIncrease;
    }

    /**
     * Gets the defense increase for this attack type.
     *
     * @return the defense increase
     */
    public int getDefensiveIncrease() {
        return defensiveIncrease;
    }

    /**
     * Gets the aggressive increase for this attack type.
     *
     * @return the aggressive increase
     */
    public int getAggressiveIncrease() {
        return aggressiveIncrease;
    }

}