package net.edge.world.entity.actor.combat.formula;

import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.attack.FightType;

public final class MagicFormula implements FormulaModifier<Actor> {

    @Override
    public int modifyAccuracy(Actor attacker, Actor defender, int roll) {
        int level = attacker.getSkillLevel(Skills.MAGIC);
        return 8 + attacker.getCombat().modifyMagicLevel(defender, level);
    }

    @Override
    public int modifyAggressive(Actor attacker, Actor defender, int roll) {
        int level = attacker.getSkillLevel(Skills.MAGIC);
        return 8 + attacker.getCombat().modifyMagicLevel(defender, level);
    }

    @Override
    public int modifyDefensive(Actor attacker, Actor defender, int roll) {
        FightType fightType = defender.getCombat().getFightType();

        int magic = defender.getSkillLevel(Skills.MAGIC);
        magic = defender.getCombat().modifyMagicLevel(attacker, magic);

        int defence = defender.getSkillLevel(Skills.DEFENCE);
        defence = defender.getCombat().modifyDefenceLevel(attacker, defence);

        int effectiveLevel = 8 + fightType.getStyle().getDefensiveIncrease();
        effectiveLevel += 0.70 * magic + 0.30 * defence;

        return effectiveLevel;
    }

    @Override
    public int modifyDamage(Actor attacker, Actor defender, int damage) {
        int bonus = attacker.getBonus(CombatConstants.BONUS_MAGIC_DAMAGE);
        return damage + damage * bonus / 100;
    }

    @Override
    public int modifyOffensiveBonus(Actor attacker, Actor defender, int bonus) {
        bonus = attacker.getBonus(CombatConstants.ATTACK_MAGIC);
        return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Actor attacker, Actor defender, int bonus) {
        return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Actor attacker, Actor defender, int bonus) {
        bonus = attacker.getBonus(CombatConstants.DEFENCE_MAGIC);
        return attacker.getCombat().modifyDefensiveBonus(defender, bonus);
    }

}
