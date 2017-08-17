package net.edge.content.combat.weapon;

import net.edge.content.combat.CombatType;
import net.edge.content.skill.Skills;

/**
 * The enumerated type whose elements represent the fighting styles.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum FightStyle {
    ACCURATE(3, 0, 0) {
        @Override
        public int[] skills(CombatType type) {
            return type == CombatType.RANGED ? new int[]{Skills.RANGED} : new int[]{Skills.ATTACK};
        }
    },
    AGGRESSIVE(0, 3, 0) {
        @Override
        public int[] skills(CombatType type) {
            return type == CombatType.RANGED ? new int[]{Skills.RANGED} : new int[]{Skills.STRENGTH};
        }
    },
    DEFENSIVE(0, 0, 3) {
        @Override
        public int[] skills(CombatType type) {
            return type == CombatType.RANGED ? new int[]{Skills.RANGED, Skills.DEFENCE} : new int[]{Skills.DEFENCE};
        }
    },
    CONTROLLED(1, 1, 1) {
        @Override
        public int[] skills(CombatType type) {
            return new int[]{Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE};
        }
    };
    /** The increase to accuracy. */
    private int accuracyIncrease;

    /** The increase to defense. */
    private int defensiveIncrease;

    /** The increase to strength. */
    private int strengthIncrease;

    /**
     * Constructs a new {@link FightStyle} element.
     *
     * @param accuracyIncrease  The accuracy increase amount.
     * @param defensiveIncrease The defensive increase amount.
     */
    FightStyle(int accuracyIncrease, int defensiveIncrease, int strengthIncrease) {
        this.accuracyIncrease = accuracyIncrease;
        this.defensiveIncrease = defensiveIncrease;
        this.strengthIncrease = strengthIncrease;
    }

    public abstract int[] skills(CombatType type);

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