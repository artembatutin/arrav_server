package net.edge.content.newcombat.attack;

import net.edge.content.newcombat.CombatConstants;

/**
 * An element of the {@link AttackStyle} enumeration represents the attack style
 * chosen by the entity during combat.
 *
 * @author Michael | Chex
 */
public enum AttackStyle {

    /** The {@code STAB} attack style. */
    STAB(CombatConstants.STAB_OFFENSE, CombatConstants.STAB_DEFENSE),

    /** The {@code SLASH} attack style. */
    SLASH(CombatConstants.SLASH_OFFENSE, CombatConstants.SLASH_DEFENSE),

    /** The {@code CRUSH} attack style. */
    CRUSH(CombatConstants.CRUSH_OFFENSE, CombatConstants.CRUSH_DEFENSE),

    /** The {@code RANGED} attack style. */
    RANGED(CombatConstants.RANGED_OFFENSE, CombatConstants.RANGED_DEFENSE),

    /** The {@code MAGIC} attack style. */
    MAGIC(CombatConstants.MAGIC_OFFENSE, CombatConstants.MAGIC_DEFENSE),

    /** The {@code DRAGONFIRE} attack style. */
    DRAGONFIRE(-1, -1);

    /** The offensive slot. */
    private int offensiveSlot;

    /** The defensive slot. */
    private int defensiveSlot;

    /**
     * Constructs a new {@link AttackStyle} element.
     *
     * @param offensiveSlot The offensive slot.
     * @param defensiveSlot The defensive slot.
     */
    AttackStyle(int offensiveSlot, int defensiveSlot) {
        this.offensiveSlot = offensiveSlot;
        this.defensiveSlot = defensiveSlot;
    }

    /**
     * Gets the offensive slot for this {@code AttackStyle} in the bonuses
     * array.
     *
     * @return The offensive slot.
     */
    public int getOffensiveSlot() {
        return offensiveSlot;
    }

    /**
     * Gets the defensive slot for this {@code AttackStyle} in the bonuses
     * array.
     *
     * @return The defensive slot.
     */
    public int getDefensiveSlot() {
        return defensiveSlot;
    }

}
