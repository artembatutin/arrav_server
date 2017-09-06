package net.edge.world.entity.actor.combat.attack;

public final class CombatModifier {

	private double attack;
	private double defence;
	private double damage;
	
	public CombatModifier attack(double percentage) {
		attack = percentage;
		return this;
	}
	
	public CombatModifier defence(double percentage) {
		defence = percentage;
		return this;
	}
	
	public CombatModifier damage(double percentage) {
		damage = percentage;
		return this;
	}
	
	public CombatModifier add(CombatModifier other) {
		attack += other.attack;
		defence += other.defence;
		damage += other.damage;
		return this;
	}
	
	public CombatModifier remove(CombatModifier other) {
		attack -= other.attack;
		defence -= other.defence;
		damage -= other.damage;
		return this;
	}
	
	public double getAttack() {
		return attack;
	}
	
	public double getDefence() {
		return defence;
	}
	
	public double getDamage() {
		return damage;
	}

	@Override
	public String toString() {
		return String.format("[acc=%s, def=%s, damage=%s]", attack, defence, damage);
	}
}
