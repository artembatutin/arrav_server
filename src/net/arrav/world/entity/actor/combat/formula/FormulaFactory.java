package net.arrav.world.entity.actor.combat.formula;

import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.hit.HitIcon;
import net.arrav.world.entity.actor.combat.hit.Hitsplat;

/**
 * Supplies factory methods useful for combat.
 *
 * @author Michael | Chex
 */
public final class FormulaFactory {

    public static Hit nextMeleeHit(Actor attacker, Actor defender) {
        int max = getMaxHit(attacker, defender, CombatType.MELEE);
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE);
    }

    public static Hit nextMeleeHit(Actor attacker, Actor defender, int max) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE);
    }

    public static Hit nextRangedHit(Actor attacker, Actor defender, int max) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.RANGED);
    }

    public static Hit nextMagicHit(Actor attacker, Actor defender, int max) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MAGIC);
    }

    private static Hit nextHit(Actor attacker, Actor defender, int max, Hitsplat hitsplat, HitIcon icon) {
        Hit hit = new Hit(0, hitsplat, icon, false, attacker.getSlot());

        if (max < 1)
            return hit;

        /*
         * Use attacker's strategy since formulas are dependent on the main combat
         * type of the attack. This allows for melee-based magic attacks,
         * ranged-based melee attacks, etc.
         */
        if (isAccurate(attacker, defender, attacker.getStrategy().getCombatType())) {
            int verdict = RandomUtils.inclusive(0, max);
            verdict = attacker.getCombat().modifyDamage(defender, verdict);

            if (verdict > defender.getCurrentHealth()) {
                verdict = defender.getCurrentHealth();
            }

            hit.setDamage(verdict);
            hit.setAccurate(true);


            if (verdict * 100 / max > 95) {
                hit.set(Hitsplat.CRITICAL);
            }
        }

        return hit;
    }

    private static boolean isAccurate(Actor attacker, Actor defender, CombatType type) {
        double attack = rollOffensive(attacker, defender, type.getFormula());
        double defence = rollDefensive(attacker, defender, type.getFormula());
        /*

            Old accuracy calc, if you prefer it

            if (defence > attack) {
                double chance = (attack - 1) / (2 * defence);
                return chance >= 1 || RandomUtils.success(chance);
            } else {
                double chance = 1 - (defence + 1) / (2 * attack);
                return chance >= 1 || RandomUtils.success(chance);
            }

         */
        return RandomUtils.success(attack / (attack + defence));
    }

    public static int rollOffensive(Actor attacker, Actor defender, FormulaModifier<Actor> formula) {
        int roll = formula.modifyAccuracy(attacker, defender, 0);
        int bonus = formula.modifyOffensiveBonus(attacker, defender, 0);
        return roll * (bonus + 64);
    }

    public static int rollDefensive(Actor attacker, Actor defender, FormulaModifier<Actor> formula) {
        int roll = formula.modifyDefensive(attacker, defender, 0);
        int bonus = formula.modifyDefensiveBonus(attacker, defender, 0);
        return roll * (bonus + 64);
    }

    public static int getMaxHit(Actor attacker, Actor defender, CombatType type) {
        FormulaModifier<Actor> formula = type.getFormula();
        int level = formula.modifyAggressive(attacker, defender, 0);
        int bonus = formula.modifyAggressiveBonus(attacker, defender, 0);
        return maxHit(level, bonus);
    }

    private static int maxHit(int level, int bonus) {
        return (320 + level * (bonus + 64)) / 64;
    }

}
