package net.edge.world.entity.actor.combat.attack;//package combat.attack;

import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.combat.hit.Hitsplat;
import net.edge.world.entity.actor.player.Player;

/**
 * Supplies factory methods useful for combat.
 *
 * @author Michael | Chex
 */
public final class FormulaFactory {

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
            int max = getMaxHit(attacker, CombatType.MELEE);
            verdict = random(max);
            verdict += verdict * attacker.getCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            } else {
                verdict = 0;
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
    public static Hit nextMeleeHit(Actor attacker, Actor defender, int max) {
        int verdict = 0;
        Hitsplat hitsplat = Hitsplat.NORMAL;

        if (isAccurate(attacker, defender, CombatType.MELEE)) {
            verdict = random(max);
            verdict += verdict * attacker.getCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            } else {
                verdict = 0;
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
            int max = getMaxHit(attacker, CombatType.RANGED);

            verdict = random(max);
            verdict += verdict * attacker.getCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            } else {
                verdict = 0;
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
    public static Hit nextRangedHit(Actor attacker, Actor defender, int max) {
        int verdict = 0;
        Hitsplat hitsplat = Hitsplat.NORMAL;

        if (isAccurate(attacker, defender, CombatType.RANGED)) {
            verdict = random(max);
            verdict += verdict * attacker.getCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            } else {
                verdict = 0;
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
            verdict += verdict * attacker.getCombat().getDamageModifier();

            if (verdict > 0) {
                if (verdict > defender.getCurrentHealth()) {
                    verdict = defender.getCurrentHealth();
                }
            } else {
                verdict = 0;
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

        double attackRoll = roll(attacker, accuracy, type, true);
        double defenceRoll = roll(defender, defence, type, false);
        double chance;

        if (attackRoll > defenceRoll) {
            chance = 1 - (defenceRoll + 2) / (2 * (attackRoll + 1));
        } else {
            chance = attackRoll / (2 * (defenceRoll + 1));
        }

//        int attChance = (int) (chance * 1000);
//        int defChance = 1000 - attChance;
//
//        if (attacker.isPlayer()) {
//            System.out.println("[PLAYER] " + attacker.toPlayer().getFormatUsername() + "  ---  " + ((int) (chance * 10000) / 100.0) + "% accuracy | attack roll: " + attackRoll + " -- defence roll: " + defenceRoll + " -- attack chance: " + attChance + " -- defence chance: " + defChance);
//        } else {
//            System.out.println("[NPC] " + attacker.toMob().getDefinition().getName() + "  ---  " + ((int) (chance * 10000) / 100.0) + "% accuracy | attack roll: " + attackRoll + " -- defence roll: " + defenceRoll + " -- attack chance: " + attChance + " -- defence chance: " + defChance);
//        }

        return RandomUtils.success(chance);
    }

    /**
     * Gets the effective accuracy level for a actor based on a combat type.
     *
     * @param actor the actor
     * @param type  the combat type
     * @return the effective accuracy
     */
    private static int getEffectiveAccuracy(Actor actor, CombatType type) {
        double modifier = actor.getCombat().getAccuracyModifier();
        if (type == CombatType.RANGED) {
            return getEffectiveRanged(actor, modifier);
        } else if (type == CombatType.MAGIC) {
            return getEffectiveMagic(actor, modifier);
        } else {
            return getEffectiveAttack(actor, modifier);
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
        double modifier = actor.getCombat().getAggressiveModifier();
        if (type == CombatType.RANGED) {
            return getEffectiveRanged(actor, modifier);
        } else if (type == CombatType.MAGIC) {
            return getEffectiveMagic(actor, modifier);
        } else {
            return getEffectiveStrength(actor, modifier);
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
        double modifier = actor.getCombat().getDefensiveModifier();
        if (type == CombatType.MAGIC) {
            return (int) (getEffectiveMagic(actor, modifier) * 0.70 + getEffectiveDefence(actor, modifier) * 0.30);
        } else {
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
        level += actor.getCombat().getFightType().getStyle().getAggressiveIncrease();
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

    public static int getMaxHit(Actor attacker, CombatType type) {
        int level = getEffectiveStrength(attacker, type);
        int bonus;

        switch (type) {
            case MELEE:
                bonus = attacker.getBonus(CombatConstants.BONUS_STRENGTH);
                return maxHit(level, bonus);
            case RANGED:
                bonus = attacker.getBonus(CombatConstants.BONUS_RANGED_STRENGTH);
                return maxHit(level, bonus);
            case MAGIC:
                bonus = attacker.getBonus(CombatConstants.BONUS_MAGIC_DAMAGE);
                return maxHit(level, 0) * bonus / 100;
        }

        throw new IllegalArgumentException("Combat type not found: " + type);
    }

    /**
     * Gets the max hit based on level and bonus.
     *
     * @param level the aggressive combat skill level
     * @param bonus the total item bonuses
     * @return the max hit
     */
    private static int maxHit(double level, double bonus) {
        float damage = 1 + 1 / 3.0F;
        damage += level / 10.0;
        damage += bonus / 80.0;
        damage += level * bonus / 640.0;
        return Math.round(damage * 10);
    }

    /**
     * Generates a roll boundary for a specific {@code Actor}.
     *
     * @param actor the actor to roll for
     * @param level the level
     * @return the roll
     */
    private static double roll(Actor actor, double level, CombatType type, boolean offensive) {
        FightType fightType = actor.getCombat().getFightType();

        if (offensive) {
            if (actor.isPlayer()) {
                Player player = actor.toPlayer();
                int bonus = player.getEquipment().getBonuses()[fightType.getBonus()];
                if (type == CombatType.MAGIC) {
                    bonus = player.getEquipment().getBonuses()[CombatConstants.ATTACK_MAGIC];
                    return roll(level, bonus, 0);
                }
                return roll(level, bonus, fightType.getStyle().getAccuracyIncrease());
            }

            int bonus = 0;
            if (type == CombatType.RANGED) {
                bonus = actor.toMob().getDefinition().getCombat().getAttackRanged();
                return roll(level, bonus, fightType.getStyle().getAccuracyIncrease());
            } else if (type == CombatType.MAGIC) {
                bonus = actor.toMob().getDefinition().getCombat().getAttackMagic();
                return roll(level, bonus, 0);
            } else if (type == CombatType.MELEE) {
                bonus = actor.toMob().getDefinition().getCombat().getAttackMelee();
                return roll(level, bonus, fightType.getStyle().getAccuracyIncrease());
            }
        }

        if (actor.isPlayer()) {
            Player player = actor.toPlayer();
            int bonus = player.getEquipment().getBonuses()[fightType.getCorrespondingBonus()];
            if (type == CombatType.MAGIC) {
                bonus = player.getEquipment().getBonuses()[CombatConstants.DEFENCE_MAGIC];
                return roll(level, bonus, 0);
            }
            return roll(level, bonus, fightType.getStyle().getDefensiveIncrease());
        }

        int bonus = 0;
        if (type == CombatType.RANGED) {
            bonus = actor.toMob().getDefinition().getCombat().getDefenceRanged();
        } else if (type == CombatType.MAGIC) {
            bonus = actor.toMob().getDefinition().getCombat().getDefenceMagic();
            return roll(level, bonus, 0);
        } else if (type == CombatType.MELEE) {
            if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_STAB) {
                bonus = actor.toMob().getDefinition().getCombat().getDefenceStab();
            } else if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_CRUSH) {
                bonus = actor.toMob().getDefinition().getCombat().getDefenceCrush();
            } else if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_SLASH) {
                bonus = actor.toMob().getDefinition().getCombat().getDefenceSlash();
            }
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
    private static int random(int max) {
        if (max <= 0) return 0;
        return RandomUtils.inclusive(max);
    }

}
