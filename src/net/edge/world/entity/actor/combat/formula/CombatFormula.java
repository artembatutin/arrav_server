package net.edge.world.entity.actor.combat.formula;

import net.edge.world.entity.actor.Actor;

import java.util.Deque;
import java.util.LinkedList;

public class CombatFormula<T extends Actor> implements FormulaModifier<T> {
    private final Deque<FormulaModifier<? super T>> modifiers = new LinkedList<>();

    public boolean add(FormulaModifier<? super T> modifier) {
        return !modifiers.contains(modifier) && modifiers.add(modifier);
    }

    public boolean remove(FormulaModifier<? super T> modifier) {
        return modifiers.remove(modifier);
    }

    @Override
    public int modifyAttackLevel(T attacker, Actor defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyAttackLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyStrengthLevel(T attacker, Actor defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyStrengthLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyDefenceLevel(Actor attacker, T defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyDefenceLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyRangedLevel(T attacker, Actor defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyRangedLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyMagicLevel(T attacker, Actor defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyMagicLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyAccuracy(T attacker, Actor defender, int roll) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            roll = modifier.modifyAccuracy(attacker, defender, roll);
        }
        return roll;
    }

    @Override
    public int modifyAggressive(T attacker, Actor defender, int roll) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            roll = modifier.modifyAggressive(attacker, defender, roll);
        }
        return roll;
    }

    @Override
    public int modifyDefensive(Actor attacker, T defender, int roll) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            roll = modifier.modifyDefensive(attacker, defender, roll);
        }
        return roll;
    }

    @Override
    public int modifyDamage(T attacker, Actor defender, int damage) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            damage = modifier.modifyDamage(attacker, defender, damage);
        }
        return damage;
    }

    @Override
    public int modifyOffensiveBonus(T attacker, Actor defender, int bonus) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            bonus = modifier.modifyOffensiveBonus(attacker, defender, bonus);
        }
        return bonus;
    }

    @Override
    public int modifyAggressiveBonus(T attacker, Actor defender, int bonus) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            bonus = modifier.modifyAggressiveBonus(attacker, defender, bonus);
        }
        return bonus;
    }

    @Override
    public int modifyDefensiveBonus(Actor attacker, T defender, int bonus) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            bonus = modifier.modifyDefensiveBonus(attacker, defender, bonus);
        }
        return bonus;
    }

}
