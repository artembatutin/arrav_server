package net.arrav.world.entity.actor.combat.formula;

import net.arrav.content.skill.Skills;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatConstants;
import net.arrav.world.entity.actor.combat.attack.FightType;

public final class MeleeFormula implements FormulaModifier<Actor> {

    @Override
    public int modifyAccuracy(Actor attacker, Actor defender, int roll) {
        FightType fightType = attacker.getCombat().getFightType();
        int level = attacker.getSkillLevel(Skills.ATTACK);
        int effectiveAccuracy = attacker.getCombat().modifyAttackLevel(defender, level);
        return 8 + effectiveAccuracy + fightType.getStyle().getAccuracyIncrease();
    }

    @Override
    public int modifyAggressive(Actor attacker, Actor defender, int roll) {
        FightType fightType = attacker.getCombat().getFightType();
        int level = attacker.getSkillLevel(Skills.STRENGTH);
        int effectiveStrength = attacker.getCombat().modifyStrengthLevel(defender, level);
        return 8 + effectiveStrength + fightType.getStyle().getAggressiveIncrease();
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
        FightType fightType = attacker.getCombat().getFightType();
        bonus = attacker.getBonus(fightType.getBonus());
        return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Actor attacker, Actor defender, int bonus) {
        bonus = attacker.getBonus(CombatConstants.BONUS_STRENGTH);
        return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Actor attacker, Actor defender, int bonus) {
        FightType fightType = attacker.getCombat().getFightType();
        bonus = defender.getBonus(fightType.getCorrespondingBonus());
        return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
    }

}
