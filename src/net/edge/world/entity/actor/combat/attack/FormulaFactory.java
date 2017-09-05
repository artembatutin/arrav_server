package net.edge.world.entity.actor.combat.attack;//package combat.attack;

import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.combat.hit.Hitsplat;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponType;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

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
            int max = getMaxHit(attacker, defender, CombatType.MELEE);
            verdict = random(max);

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
    public static Hit nextRangedHit(Actor attacker, Actor defender, RangedWeaponType type) {
        int verdict = 0;
        Hitsplat hitsplat = Hitsplat.NORMAL;

        if (isAccurate(attacker, defender, CombatType.RANGED)) {
            if (type == RangedWeaponType.THROWN) {
                Item weapon = attacker.toPlayer().getEquipment().get(Equipment.WEAPON_SLOT);
                attacker.getBonus(CombatConstants.BONUS_RANGED_STRENGTH);
                attacker.toPlayer().setBonus(CombatConstants.BONUS_RANGED_STRENGTH, weapon.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH]);
            }

            int max = getMaxHit(attacker, defender, CombatType.RANGED);
            verdict = random(max);

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
        int attackRoll = getOffensiveRoll(attacker, defender, type);
        int defenceRoll = getDefensiveRoll(attacker, defender, type);

        if (attackRoll > defenceRoll) {
            double chance = 1 - (defenceRoll + 2) / (2.0 * (attackRoll + 1));
            return RandomUtils.success(chance);
        } else {
            double chance = attackRoll / (2.0 * (defenceRoll + 1));
            return RandomUtils.success(chance);
        }
    }

    public static int getOffensiveRoll(Actor attacker, Actor defender, CombatType type) {
        int bonus = getOffensiveBonus(attacker, type);
        int roll = getEffectiveAccuracy(attacker, defender, type);
        roll = roll * (bonus + 64);
        return attacker.getCombat().modifyAccuracy(roll);
    }

    public static int getDefensiveRoll(Actor attacker, Actor defender, CombatType type) {
        int bonus = getDefensiveBonus(defender, type);
        int roll = getEffectiveDefence(attacker, defender, type);
        roll = roll * (bonus + 64);
        return attacker.getCombat().modiftDefensive(roll);
    }

    /**
     * Gets the effective accuracy level for a actor based on a combat type.
     *
     * @param attacker the actor
     * @param defender the defender
     * @param type     the combat type
     * @return the offensive roll
     */
    private static int getEffectiveAccuracy(Actor attacker, Actor defender, CombatType type) {
        FightType fightType = attacker.getCombat().getFightType();
        int effectiveAccuracy = 8 + fightType.getStyle().getAccuracyIncrease();

        if (type == CombatType.RANGED) {
            effectiveAccuracy += getAugmentedRanged(attacker);
            if (CombatUtil.isFullVoid(attacker)) {
                if (!attacker.isPlayer() || attacker.toPlayer().getEquipment().contains(11664)) {
                    effectiveAccuracy *= 1.1;
                }
            }
        } else if (type == CombatType.MELEE) {
            effectiveAccuracy += getAugmentedAttack(attacker, defender);
            if (CombatUtil.isFullVoid(attacker)) {
                if (!attacker.isPlayer() || attacker.toPlayer().getEquipment().contains(11665)) {
                    effectiveAccuracy *= 1.1;
                }
            }
        } else if (type == CombatType.MAGIC) {
            effectiveAccuracy += getAugmentedMagic(attacker);
            if (CombatUtil.isFullVoid(attacker)) {
                if (!attacker.isPlayer() || attacker.toPlayer().getEquipment().contains(11663)) {
                    effectiveAccuracy *= 1.45;
                }
            }
        }

        return effectiveAccuracy;
    }

    /**
     * Gets the effective strength level for a actor based on a combat type.
     *
     * @param attacker the actor
     * @param defender the defender
     * @param type     the combat type
     * @return the effective strength
     */
    private static int getEffectiveStrength(Actor attacker, Actor defender, CombatType type) {
        FightType fightType = attacker.getCombat().getFightType();
        int effectiveStrength = 8 + fightType.getStyle().getAggressiveIncrease();

        if (type == CombatType.RANGED) {
            effectiveStrength += getAugmentedRanged(attacker);
            if (CombatUtil.isFullVoid(attacker)) {
                if (!attacker.isPlayer() || attacker.toPlayer().getEquipment().contains(11664)) {
                    effectiveStrength *= 1.1;
                }
            }
        } else if (type == CombatType.MELEE) {
            effectiveStrength += getAugmentedStrength(attacker, defender);
            if (CombatUtil.isFullVoid(attacker)) {
                if (!attacker.isPlayer() || attacker.toPlayer().getEquipment().contains(11665)) {
                    effectiveStrength *= 1.1;
                }
            }
        } else if (type == CombatType.MAGIC) {
            effectiveStrength += getAugmentedMagic(attacker);
        }

        return effectiveStrength;
    }

    /**
     * Gets the effective defence for a actor based on a combat type.
     *
     * @param type the combat type
     * @return the effective defence
     */
    private static int getEffectiveDefence(Actor attacker, Actor defender, CombatType type) {
        if (type == CombatType.MAGIC) {
            return (7 * getAugmentedMagic(defender) + 3 * getAugmentedDefence(attacker, defender)) / 10 + 8;
        } else {
            FightType fightType = defender.getCombat().getFightType();
            int effectiveDefence = 8 + fightType.getStyle().getDefensiveIncrease();
            return effectiveDefence + getAugmentedDefence(attacker, defender);
        }
    }

    /**
     * Gets the effective attack level for a actor.
     *
     * @param attacker the actor
     * @return the effective attack level
     */
    private static int getAugmentedAttack(Actor attacker, Actor defender) {
        int level = attacker.getSkillLevel(Skills.ATTACK);

        if (attacker.isPlayer()) {
            Player player = attacker.toPlayer();

            for (Prayer prayer : player.getPrayerActive()) {
                level = prayer.modifyAccuracyLevel(player, level);
            }

            if (Prayer.isActivated(player, Prayer.TURMOIL)) {
                if (defender != null && defender.isPlayer()) {
                    // 1000 because if attack is 99 / 100 * 10 = 9.9 which is way to much.
                    level *= defender.toPlayer().getSkills()[Skills.ATTACK].getRealLevel() / 1000;
                }
            }
        }

        return level;
    }

    /**
     * Gets the effective strength level for a actor.
     *
     * @param attacker the attacker
     * @return the effective strength level
     */
    private static int getAugmentedStrength(Actor attacker, Actor defender) {
        int level = attacker.getSkillLevel(Skills.STRENGTH);

        if (attacker.isPlayer()) {
            Player player = attacker.toPlayer();

            for (Prayer prayer : player.getPrayerActive()) {
                level = prayer.modifyAggressiveLevel(player, level);
            }

            if (Prayer.isActivated(player, Prayer.TURMOIL)) {
                if (defender != null && defender.isPlayer()) {
                    // 1000 because if strength is 99 / 100 * 10 = 9.9 which is way to much.
                    level *= defender.toPlayer().getSkills()[Skills.STRENGTH].getRealLevel() / 1000;
                }
            }

        }

        return level;
    }

    /**
     * Gets the effective defence for a actor.
     *
     * @return the effective defence
     */
    private static int getAugmentedDefence(Actor attacker, Actor defender) {
        int level = defender.getSkillLevel(Skills.DEFENCE);

        if (defender.isPlayer()) {
            Player player = defender.toPlayer();

            for (Prayer prayer : player.getPrayerActive()) {
                level = prayer.modifyDefensiveLevel(player, level);
            }

            if (Prayer.isActivated(player, Prayer.TURMOIL)) {
                if (attacker != null && attacker.isPlayer()) {
                    // 1000 because if defence is 99 / 100 * 10 = 9.9 which is way to much.
                    level *= attacker.toPlayer().getSkills()[Skills.DEFENCE].getRealLevel() / 1000;
                }
            }
        }

        return level;
    }

    /**
     * Gets the effective ranged level for a actor.
     *
     * @param actor the actor
     * @return the effective ranged level
     */
    private static int getAugmentedRanged(Actor actor) {
        int level = actor.getSkillLevel(Skills.RANGED);

        if (actor.isPlayer()) {
            Player player = actor.toPlayer();

            for (Prayer prayer : player.getPrayerActive()) {
                level = prayer.modifyAccuracyLevel(player, level);
            }
        }

        return level;
    }

    /**
     * Augments an actor's magic level with prayer bonuses.
     *
     * @param actor the actor
     * @return the augmented magic level
     */
    private static int getAugmentedMagic(Actor actor) {
        int level = actor.getSkillLevel(Skills.MAGIC);

        if (actor.isPlayer()) {
            Player player = actor.toPlayer();

            for (Prayer prayer : player.getPrayerActive()) {
                level = prayer.modifyAccuracyLevel(player, level);
            }
        }

        return level;
    }

    public static int getMaxHit(Actor attacker, CombatType type) {
        return getMaxHit(attacker, null, type);
    }

    public static int getMaxHit(Actor attacker, Actor defender, CombatType type) {
        int level = getEffectiveStrength(attacker, defender, type);
        level = attacker.getCombat().modifyAggressive(level);

        int max, bonus;
        if (type == CombatType.MELEE) {
            bonus = attacker.getBonus(CombatConstants.BONUS_STRENGTH);
            max = maxHit(level, bonus);
        } else if (type == CombatType.RANGED) {
            bonus = attacker.getBonus(CombatConstants.BONUS_RANGED_STRENGTH);
            max = maxHit(level, bonus);
        } else if (type == CombatType.MAGIC) {
            bonus = attacker.getBonus(CombatConstants.BONUS_MAGIC_DAMAGE);
            max = maxHit(level, 0) * bonus / 100;
        } else {
            throw new IllegalArgumentException("Combat type not found: " + type);
        }

        if (attacker.isPlayer()) {
            Player player = attacker.toPlayer();

            for (Prayer prayer : player.getPrayerActive()) {
                max = prayer.modifyDamage(player, max);
            }
        }

        attacker.getCombat().modifyDamage(max);
        return max;

    }

    /**
     * Gets the max hit based on level and bonus.
     *
     * @param level the strength level
     * @param bonus the total strength bonuses
     * @return the max hit
     */
    private static int maxHit(int level, int bonus) {
        return 5 + level + level * bonus / 64;
    }

    /**
     * Generates an offensive bonus for an actor given the combat type used.
     *
     * @param actor the actor to roll for
     * @param type  the combat type to attack with
     * @return the offensive bonus
     */
    private static int getOffensiveBonus(Actor actor, CombatType type) {
        FightType fightType = actor.getCombat().getFightType();

        if (actor.isPlayer()) {
            Player player = actor.toPlayer();
            int[] bonuses = player.getEquipment().getBonuses();

            if (type == CombatType.MAGIC) {
                return bonuses[CombatConstants.ATTACK_MAGIC];
            }

            return bonuses[fightType.getBonus()];
        }

        int bonus = 0;
        if (type == CombatType.MELEE) {
            bonus = actor.toMob().getDefinition().getCombat().getAttackMelee();
        } else if (type == CombatType.RANGED) {
            bonus = actor.toMob().getDefinition().getCombat().getAttackRanged();
        } else if (type == CombatType.MAGIC) {
            bonus = actor.toMob().getDefinition().getCombat().getAttackMagic();
        }

        return bonus;
    }

    /**
     * Generates a defensive roll for an actor given the defensive skill level
     * and combat type used.
     *
     * @param actor the actor to roll for
     * @param type  the combat type to defend against
     * @return the defensive attack roll
     */
    private static int getDefensiveBonus(Actor actor, CombatType type) {
        FightType fightType = actor.getCombat().getFightType();

        if (actor.isPlayer()) {
            Player player = actor.toPlayer();
            int[] bonuses = player.getEquipment().getBonuses();

            if (type == CombatType.MAGIC) {
                return bonuses[CombatConstants.DEFENCE_MAGIC];
            }

            return bonuses[fightType.getCorrespondingBonus()];
        }

        if (type == CombatType.MELEE) {
            if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_STAB) {
                return actor.toMob().getDefinition().getCombat().getDefenceStab();
            } else if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_CRUSH) {
                return actor.toMob().getDefinition().getCombat().getDefenceCrush();
            } else if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_SLASH) {
                return actor.toMob().getDefinition().getCombat().getDefenceSlash();
            }
        } else if (type == CombatType.RANGED) {
            return actor.toMob().getDefinition().getCombat().getDefenceRanged();
        } else if (type == CombatType.MAGIC) {
            return actor.toMob().getDefinition().getCombat().getDefenceMagic();
        }

        return 0;
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
