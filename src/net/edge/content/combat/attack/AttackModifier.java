package net.edge.content.combat.attack;

public final class AttackModifier {

	private double accuracy;
	private double aggressive;
	private double defensive;
	private double damage;

	public AttackModifier accuracy(double percentage) {
		accuracy = percentage;
		return this;
	}

	public AttackModifier aggressive(double percentage) {
		aggressive = percentage;
		return this;
	}

	public AttackModifier defensive(double percentage) {
		defensive = percentage;
		return this;
	}

	public AttackModifier damage(double percentage) {
		damage = percentage;
		return this;
	}

	public AttackModifier add(AttackModifier other) {
		accuracy += other.accuracy;
		aggressive += other.aggressive;
		defensive += other.defensive;
		damage += other.damage;
		return this;
	}

	public AttackModifier remove(AttackModifier other) {
		accuracy -= other.accuracy;
		aggressive -= other.aggressive;
		defensive -= other.defensive;
		damage -= other.damage;
		return this;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getAggressive() {
		return aggressive;
	}

	public double getDefensive() {
		return defensive;
	}

	public double getDamage() {
		return damage;
	}

}
