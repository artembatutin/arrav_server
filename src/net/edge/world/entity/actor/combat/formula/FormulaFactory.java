package net.edge.world.entity.actor.combat.formula;

import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.combat.hit.Hitsplat;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponType;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

/**
 * Supplies factory methods useful for combat.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Michael | Chex
 */
public final class FormulaFactory {
	
	/**
	 * Formulas stored in an array for quick access on type index.
	 */
	private final static Formula[] FORMULAS = {
			new MeleeFormula(),
			new RangedFormula(),
			new MagicFormula()
	};
	
	/**
	 * Builds the next hit for the {@code attacker}. If the hit is accurate,
	 * then the max hit formula will calculate a random number to be generated
	 * from a range of range [0, max].
	 * @return a {@code Hit} representing the damage done
	 */
	public static Hit nextMeleeHit(Actor attacker, Actor defender) {
		int verdict = 0;
		Hitsplat hitsplat = Hitsplat.NORMAL;
		if(isAccurate(attacker, defender, CombatType.MELEE)) {
			verdict = random(maxHit(attacker, defender, CombatType.MELEE, 0));
			if(verdict > 0) {
				if(verdict > defender.getCurrentHealth())
					verdict = defender.getCurrentHealth();
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
	 * @return a {@code Hit} representing the damage done
	 */
	public static Hit nextMeleeHit(Actor attacker, Actor defender, int max) {
		int verdict = 0;
		Hitsplat hitsplat = Hitsplat.NORMAL;
		if(isAccurate(attacker, defender, CombatType.MELEE)) {
			verdict = random(max);
			if(verdict > 0) {
				if(verdict > defender.getCurrentHealth()) {
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
	 * @return a {@code Hit} representing the damage done
	 */
	public static Hit nextRangedHit(Actor attacker, Actor defender, RangedWeaponType type) {
		int verdict = 0;
		Hitsplat hitsplat = Hitsplat.NORMAL;
		if(isAccurate(attacker, defender, CombatType.RANGED)) {
			if(type == RangedWeaponType.THROWN) {
				Item weapon = attacker.toPlayer().getEquipment().get(Equipment.WEAPON_SLOT);
				attacker.getBonus(CombatConstants.BONUS_RANGED_STRENGTH);
				attacker.toPlayer().setBonus(CombatConstants.BONUS_RANGED_STRENGTH, weapon.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH]);
			}
			verdict = random(maxHit(attacker, defender, CombatType.RANGED, 0));
			if(verdict > 0) {
				if(verdict > defender.getCurrentHealth()) {
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
	 * @return a {@code Hit} representing the damage done
	 */
	public static Hit nextRangedHit(Actor attacker, Actor defender, int max) {
		int verdict = 0;
		Hitsplat hitsplat = Hitsplat.NORMAL;
		if(isAccurate(attacker, defender, CombatType.RANGED)) {
			verdict = random(max);
			if(verdict > 0) {
				if(verdict > defender.getCurrentHealth()) {
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
	 * @return a {@code Hit} representing the damage done
	 */
	public static Hit nextMagicHit(Actor attacker, Actor defender, int max) {
		int verdict = 0;
		Hitsplat hitsplat = Hitsplat.NORMAL;
		if(isAccurate(attacker, defender, CombatType.MAGIC)) {
			verdict = random(max);
			if(verdict > 0) {
				if(verdict > defender.getCurrentHealth()) {
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
	 * @param attacker the attacking entity
	 * @param defender the defending entity
	 * @param type     the combat type
	 * @return {@code true} if the roll was accurate
	 */
	private static boolean isAccurate(Actor attacker, Actor defender, CombatType type) {
		if(type == CombatType.NONE) {
			return true;
		}
		Formula formula = FORMULAS[type.ordinal()];
		double attack = attacker.getCombat().modAttack(formula.attack(attacker, defender));
		double defence = defender.getCombat().modDefence(formula.defence(attacker, defender));
		if(defence > attack) {
			double chance = (attack - 1D) / (2D * defence);
			return chance >= 1 || RandomUtils.success(chance);
		} else {
			double chance = 1 - (defence + 1) / (2 * attack);
			return chance >= 1 || RandomUtils.success(chance);
		}
	}
	
	/**
	 * Calculates a max hit depending on the formula combat type.
	 * @param attacker the attacking entity
	 * @param defender the defending entity
	 * @param type     the combat type
	 * @return max hit calculated.
	 */
	public static int maxHit(Actor attacker, Actor defender, CombatType type) {
		return maxHit(attacker, defender, type, 0);
	}
	
	/**
	 * Calculates a max hit depending on the formula combat type.
	 * @param attacker the attacking entity
	 * @param defender the defending entity
	 * @param type     the combat type
	 * @param max      the starting max hit given.
	 * @return max hit calculated.
	 */
	public static int maxHit(Actor attacker, Actor defender, CombatType type, int max) {
		if(max > 0) {
			return max;
		}
		if(type == CombatType.NONE) {
			return 0;
		}
		Formula formula = FORMULAS[type.ordinal()];
		return attacker.getCombat().modDamage(formula.maxHit(attacker, defender));
	}
	
	/**
	 * Generates a pseudo-random number with the lower bound being {@code min}
	 * and the upper bound being {@code max}. The inclusive state will be the
	 * interval {@code [min, max]} if {@code true}, or {@code [min, max)} if
	 * {@code false}.
	 * @param max the upper bound
	 * @return a pseudo-random number
	 */
	private static int random(int max) {
		if (max <= 0)
			return 0;
		return RandomUtils.inclusive(max);
	}
	
}
