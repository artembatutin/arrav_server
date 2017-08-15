package net.edge.content.newcombat.attack;

/**
 * An element of the {@link AttackStance} enumeration represents the stance
 * chosen by the entity during combat.
 *
 * @author Michael | Chex
 */
public enum AttackStance {

    /** The accurate attack type. */
    ACCURATE(3, 0, 0),

    /** The aggressive attack type. */
    AGGRESSIVE(0, 0, 3),

    /** The defensive attack type. */
    DEFENSIVE(0, 3, 0),

    /** The controlled attack type. */
    CONTROLLED(1, 1, 1),

    /** The rapid attack type. */
    RAPID(0, 0, 0),

    /** The longrange attack type. */
    LONGRANGE(0, 0, 0);

    /** The increase to accuracy. */
    private int accuracyIncrease;

    /** The increase to defense. */
    private int defensiveIncrease;

    /** The increase to strength. */
    private int strengthIncrease;

    /**
     * Constructs a new {@link AttackStance} element.
     *
     * @param accuracyIncrease  The accuracy increase amount.
     * @param defensiveIncrease The defensive increase amount.
     */
    AttackStance(int accuracyIncrease, int defensiveIncrease, int strengthIncrease) {
        this.accuracyIncrease = accuracyIncrease;
        this.defensiveIncrease = defensiveIncrease;
        this.strengthIncrease = strengthIncrease;
    }

    /**
     * Gets the accuracy increase for this attack type.
     *
     * @return The accuracy increase.
     */
    public int getAccuracyIncrease() {
        return accuracyIncrease;
    }

    /**
     * Gets the defense increase for this attack type.
     *
     * @return The defense increase.
     */
    public int getDefensiveIncrease() {
        return defensiveIncrease;
    }

    /**
     * Gets the strength increase for this attack type.
     *
     * @return The strength increase.
     */
    public int getStrengthIncrease() {
        return strengthIncrease;
    }

}
