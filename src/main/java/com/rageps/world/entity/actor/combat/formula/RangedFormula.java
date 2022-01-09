package com.rageps.world.entity.actor.combat.formula;

import com.rageps.content.skill.Skills;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatConstants;
import com.rageps.world.entity.actor.combat.attack.FightType;

public final class RangedFormula implements FormulaModifier<Actor> {

	@Override
	public int modifyAccuracy(Actor attacker, Actor defender, int roll) {
		FightType fightType = attacker.getCombat().getFightType();
		int level = attacker.getSkillLevel(Skills.RANGED);
		int effectiveAccuracy = attacker.getCombat().modifyRangedLevel(defender, level);
		return 8 + effectiveAccuracy + fightType.getStyle().getAccuracyIncrease();
	}

	@Override
	public int modifyAggressive(Actor attacker, Actor defender, int roll) {
		int level = attacker.getSkillLevel(Skills.RANGED);
		return 8 + attacker.getCombat().modifyRangedLevel(defender, level);
	}

	@Override
	public int modifyDefensive(Actor attacker, Actor defender, int roll) {
		FightType fightType = defender.getCombat().getFightType();
		int level = defender.getSkillLevel(Skills.DEFENCE);
		int effectiveDefence = defender.getCombat().modifyDefenceLevel(attacker, level);
		return 8 + effectiveDefence + fightType.getStyle().getDefensiveIncrease();
	}

	@Override
	public int modifyOffensiveBonus(Actor attacker, Actor defender, int bonus) {
		bonus = attacker.getBonus(CombatConstants.ATTACK_RANGED);
		return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
	}

	@Override
	public int modifyAggressiveBonus(Actor attacker, Actor defender, int bonus) {
		bonus = attacker.getBonus(CombatConstants.BONUS_RANGED_STRENGTH);
		return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
	}

	@Override
	public int modifyDefensiveBonus(Actor attacker, Actor defender, int bonus) {
		bonus = defender.getBonus(CombatConstants.DEFENCE_RANGED);
		return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
	}

}
