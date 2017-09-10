package net.edge.world.entity.actor.combat.attack;

import net.edge.world.entity.actor.Actor;

import java.util.function.Function;

public final class CombatModifier {
	private static final Function<Double, ModifierListener> MULTIPLY = percent -> (attacker, defender, level) -> level += Math.round(level * percent);
	private static final Function<Integer, ModifierListener> ADDITION = amount -> (attacker, defender, level) -> level += amount;

	private ModifierListener attackModifier = ModifierListener.identity();
	private ModifierListener defensiveModifier = ModifierListener.identity();
	private ModifierListener damageModifier = ModifierListener.identity();

    /* ********************************************************************** */

	public CombatModifier attack(double percent) {
        attackModifier = MULTIPLY.apply(percent);
		return this;
	}

	public CombatModifier defence(double percent) {
		defensiveModifier = MULTIPLY.apply(percent);
		return this;
	}

	public CombatModifier damage(double percent) {
		damageModifier = MULTIPLY.apply(percent);
		return this;
	}

    /* ********************************************************************** */

	public CombatModifier attack(int amount) {
        attackModifier = ADDITION.apply(amount);
		return this;
	}

	public CombatModifier defence(int amount) {
		defensiveModifier = ADDITION.apply(amount);
		return this;
	}

	public CombatModifier damage(int amount) {
		damageModifier = ADDITION.apply(amount);
		return this;
	}

    /* ********************************************************************** */

	public CombatModifier attack(ModifierListener listener) {
        attackModifier = listener;
		return this;
	}

	public CombatModifier defence(ModifierListener listener) {
		defensiveModifier = listener;
		return this;
	}

	public CombatModifier damage(ModifierListener listener) {
		damageModifier = listener;
		return this;
	}

    /* ********************************************************************** */

	public int modifyAttack(Actor attacker, Actor defender, int roll) {
		return attackModifier.modify(attacker, defender, roll);
	}

	public int modifyDefence(Actor attacker, Actor defender, int roll) {
		return defensiveModifier.modify(attacker, defender, roll);
	}

	public int modifyDamage(Actor attacker, Actor defender, int damage) {
		return this.damageModifier.modify(attacker, defender, damage);
	}

}
