package com.rageps.world.entity.actor.combat.formula;

import com.rageps.world.entity.actor.Actor;

public interface FormulaModifier<T extends Actor> {
	
	default int modifyAttackLevel(T attacker, Actor defender, int level) {
		return level;
	}
	
	default int modifyStrengthLevel(T attacker, Actor defender, int level) {
		return level;
	}
	
	default int modifyDefenceLevel(Actor attacker, T defender, int level) {
		return level;
	}
	
	default int modifyRangedLevel(T attacker, Actor defender, int level) {
		return level;
	}
	
	default int modifyMagicLevel(T attacker, Actor defender, int level) {
		return level;
	}
	
	default int modifyAccuracy(T attacker, Actor defender, int roll) {
		return roll;
	}
	
	default int modifyAggressive(T attacker, Actor defender, int roll) {
		return roll;
	}
	
	default int modifyDefensive(Actor attacker, T defender, int roll) {
		return roll;
	}
	
	default int modifyDamage(T attacker, Actor defender, int damage) {
		return damage;
	}
	
	default int modifyOffensiveBonus(T attacker, Actor defender, int bonus) {
		return bonus;
	}
	
	default int modifyAggressiveBonus(T attacker, Actor defender, int bonus) {
		return bonus;
	}
	
	default int modifyDefensiveBonus(Actor attacker, T defender, int bonus) {
		return bonus;
	}
	
}
