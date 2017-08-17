package net.edge.content.combat.formula;//package combat.attack;

import net.edge.content.combat.CombatConstants;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.hit.Hitsplat;
import net.edge.content.combat.attack.FightType;
import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

import java.security.SecureRandom;

/**
 * Supplies factory methods useful for combat.
 *
 * @author Michael | Chex
 */
public final class FormulaFactory {

    /** A pseudo-random number generator instance */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Builds the next hit for the {@code attacker}. If the hit is accurate,
     * then the max hit formula will calculate a random number to be generated
     * from a range of range [0, max].
     *
     * @return a {@code Hit} representing the damage done
     */
    public static Hit nextMeleeHit(Actor attacker, Actor defender) {
        int verdict = 0;
        Hitsplat hitsplat = Hitsplat.NORMAL;
        if (isAccurate(attacker, defender, CombatType.MELEE)) {
            int level = getEffectiveStrength(attacker, CombatType.MELEE);
            int bonus = attacker.getBonus(CombatConstants.BONUS_STRENGTH);
            int max = maxHit(level, bonus);

            verdict = random(max);
            verdict += verdict * attacker.getNewCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            }

            return new Hit(verdict, hitsplat, HitIcon.MELEE, true);
        }
        return new Hit(verdict, hitsplat, HitIcon.MELEE, false);
    }

    /**
     * Builds the next hit for the {@code attacker}. If the hit is accurate,
     * then the max hit formula will calculate a random number to be generated
     * from a range of range [0, max].
     *
     * @return a {@code Hit} representing the damage done
     */
    public static Hit nextRangedHit(Actor attacker, Actor defender) {
        int verdict = 0;
        Hitsplat hitsplat = Hitsplat.NORMAL;
        if (isAccurate(attacker, defender, CombatType.RANGED)) {
            int level = getEffectiveStrength(attacker, CombatType.RANGED);
            int bonus = attacker.getBonus(CombatConstants.BONUS_RANGED_STRENGTH);
            int max = maxHit(level, bonus);

            verdict = random(max);
            verdict += verdict * attacker.getNewCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            }

            return new Hit(verdict, hitsplat, HitIcon.RANGED, true);
        }
        return new Hit(verdict, hitsplat, HitIcon.RANGED, false);
    }

    /**
     * Builds the next hit for the {@code attacker}. If the hit is accurate,
     * then the max hit formula will calculate a random number to be generated
     * from a range of range [0, max].
     *
     * @return a {@code Hit} representing the damage done
     */
    public static Hit nextMagicHit(Actor attacker, Actor defender, int max) {
        int verdict = 0;
        Hitsplat hitsplat = Hitsplat.NORMAL;
        if (isAccurate(attacker, defender, CombatType.MAGIC)) {
            verdict = random(max);
            verdict += verdict * attacker.getNewCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            }

            return new Hit(verdict, hitsplat, HitIcon.MAGIC, true);
        }
        return new Hit(verdict, hitsplat, HitIcon.MAGIC, false);
    }

    /**
     * Determines if the attacker's next hit is accurate against the defender.
     *
     * @param attacker the attacking entity
     * @param defender the defending entity
     * @param type     the combat type
     * @return {@code true} if the roll was accurate
     */
    private static boolean isAccurate(Actor attacker, Actor defender, CombatType type) {
        int accuracy = getEffectiveAccuracy(attacker, type);
        int defence = getEffectiveDefence(defender, type);
        FightType attType = attacker.getNewCombat().getFightType();
        FightType defType = attacker.getNewCombat().getFightType();

        double attackRoll = roll(attacker, accuracy, attType, true);
        double defenceRoll = roll(defender, defence, defType, false);
        double chance;

        if (attackRoll < defenceRoll) {
            chance = (attackRoll - 1) / (defenceRoll * 2);
        } else {
            chance = 1 - ((defenceRoll + 1) / (attackRoll * 2));
        }

//        System.out.println(attacker.getName() + " " + accuracy + " " + attacker.getNewCombat().getAttackStance() + " " + style);
//        System.out.println(defender.getName() + " " + defence  + " " + defender.getNewCombat().getAttackStance() + " " + style);
//        System.out.println(attacker.getName() + " " + ((int) (chance * 1000) / 10.0) + "% " + attackRoll + " " + defenceRoll);
//        System.out.println();

        return random(chance * 100) > random(100 - chance * 100);
    }

    /**
     * Gets the effective accuracy level for a actor based on a combat type.
     *
     * @param actor the actor
     * @param type  the combat type
     * @return the effective accuracy
     */
    private static int getEffectiveAccuracy(Actor actor, CombatType type) {
        double modifier = actor.getNewCombat().getAccuracyModifier();
        switch (type) {
            default:
            case MELEE:
                return getEffectiveAttack(actor, modifier);
            case RANGED:
                return getEffectiveRanged(actor, modifier);
            case MAGIC:
                return getEffectiveMagic(actor, modifier);
        }
    }


    /**
     * Gets the effective strength level for a actor based on a combat type.
     *
     * @param actor the actor
     * @param type  the combat type
     * @return the effective strength
     */
    private static int getEffectiveStrength(Actor actor, CombatType type) {
        double modifier = actor.getNewCombat().getAggressiveModifier();
        switch (type) {
            default:
            case MELEE:
                return getEffectiveStrength(actor, modifier);
            case RANGED:
                return getEffectiveRanged(actor, modifier);
            case MAGIC:
                return getEffectiveMagic(actor, modifier);
        }
    }

    /**
     * Gets the effective defence for a actor based on a combat type.
     *
     * @param actor the actor
     * @param type  the combat type
     * @return the effective defence
     */
    private static int getEffectiveDefence(Actor actor, CombatType type) {
        double modifier = actor.getNewCombat().getDefensiveModifier();
        switch (type) {
            case MAGIC:
                return (int) (getEffectiveMagic(actor, modifier) * 0.70 + getEffectiveDefence(actor, modifier) * 0.30);
            default:
                return getEffectiveDefence(actor, modifier);
        }
    }

    /**
     * Gets the effective attack level for a actor.
     *
     * @param actor    the actor
     * @param modifier the multiplicative modifier to the final level
     * @return the effective attack level
     */
    private static int getEffectiveAttack(Actor actor, double modifier) {
        int level = actor.getSkillLevel(Skills.ATTACK);
        level += level * modifier;
        return level;
    }

    /**
     * Gets the effective strength level for a actor.
     *
     * @param actor    the actor
     * @param modifier the multiplicative modifier to the final level
     * @return the effective strength level
     */
    private static int getEffectiveStrength(Actor actor, double modifier) {
        int level = actor.getSkillLevel(Skills.STRENGTH);
        level += level * modifier;
        return level;
    }

    /**
     * Gets the effective defence for a actor.
     *
     * @param actor    the actor
     * @param modifier the multiplicative modifier to the final level
     * @return the effective defence
     */
    private static int getEffectiveDefence(Actor actor, double modifier) {
        int level = actor.getSkillLevel(Skills.DEFENCE);
        level += level * modifier;
        return level;
    }

    /**
     * Gets the effective ranged level for a actor.
     *
     * @param actor    the actor
     * @param modifier the multiplicative modifier to the final level
     * @return the effective ranged level
     */
    private static int getEffectiveRanged(Actor actor, double modifier) {
        int level = actor.getSkillLevel(Skills.RANGED);
        level += level * modifier;
        return level;
    }

    /**
     * Gets the effective magic level for a actor.
     *
     * @param actor    the actor
     * @param modifier the multiplicative modifier to the final level
     * @return the effective magic level
     */
    private static int getEffectiveMagic(Actor actor, double modifier) {
        int level = actor.getSkillLevel(Skills.MAGIC);
        level += level * modifier;
        return level;
    }

    /**
     * Gets the max hit based on level and bonus.
     *
     * @param level the aggressive combat skill level
     * @param bonus the total item bonuses
     * @return the max hit
     */
    public static int maxHit(double level, double bonus) {
        double damage = 1 + 1 / 3.0;
        damage += level / 10.0;
        damage += bonus / 80.0;
        damage += level * bonus / 640.0;
        return (int) damage;
    }

    /**
     * Generates a roll boundary for a specific {@code Actor}.
     *
     * @param actor     the actor to roll for
     * @param level     the level
     * @param fightType the fight type
     * @return the roll
     */
    private static double roll(Actor actor, double level, FightType fightType, boolean offensive) {
        if (offensive) {
            if (actor.isPlayer()) {
                Player player = actor.toPlayer();
                int bonus = player.getEquipment().getBonuses()[fightType.getBonus()];
                return roll(level, bonus, fightType.getStyle().getAccuracyIncrease());
            }
            int bonus;
            switch (fightType.getBonus()) {
                case CombatConstants.ATTACK_RANGED:
                    bonus = actor.toMob().getDefinition().getCombat().getAttackRanged();
                    break;

                case CombatConstants.ATTACK_MAGIC:
                    bonus = actor.toMob().getDefinition().getCombat().getAttackMagic();
                    break;

                default:
                    bonus = actor.toMob().getDefinition().getCombat().getAttackMelee();
                    break;
            }
            return roll(level, bonus, fightType.getStyle().getAccuracyIncrease());
        }

        if (actor.isPlayer()) {
            Player player = actor.toPlayer();
            int bonus = player.getEquipment().getBonuses()[fightType.getCorrespondingBonus()];
            return roll(level, bonus, fightType.getStyle().getDefensiveIncrease());
        }

        int bonus = 0;
        switch (fightType.getCorrespondingBonus()) {
            case CombatConstants.DEFENCE_RANGED:
                bonus = actor.toMob().getDefinition().getCombat().getDefenceRanged();
                break;

            case CombatConstants.DEFENCE_MAGIC:
                bonus = actor.toMob().getDefinition().getCombat().getDefenceMagic();
                break;

            case CombatConstants.DEFENCE_STAB:
                bonus = actor.toMob().getDefinition().getCombat().getDefenceStab();
                break;

            case CombatConstants.DEFENCE_CRUSH:
                bonus = actor.toMob().getDefinition().getCombat().getDefenceCrush();
                break;

            case CombatConstants.DEFENCE_SLASH:
                bonus = actor.toMob().getDefinition().getCombat().getDefenceSlash();
                break;
        }
        return roll(level, bonus, fightType.getStyle().getDefensiveIncrease());
    }

    /**
     * Generates a roll boundary, given a {@code level}, {@code bonus}, and
     * {@code stance} increase amount.
     *
     * @param level  the skill level
     * @param bonus  the total bonus
     * @param stance the stance increase to the effective level
     * @return the roll
     */
    private static double roll(double level, double bonus, int stance) {
        double effectiveLevel = level + stance + 8;
        return effectiveLevel * (bonus + 64);
    }

    /**
     * Generates a pseudo-random number with the lower bound being {@code min}
     * and the upper bound being {@code max}. The inclusive state will be the
     * interval {@code [min, max]} if {@code true}, or {@code [min, max)} if
     * {@code false}.
     *
     * @param max the upper bound
     * @return a pseudo-random number
     */
    private static int random(double max) {
        if (max <= 0) return 0;
        return (int) (RANDOM.nextDouble() * max);
    }

}
