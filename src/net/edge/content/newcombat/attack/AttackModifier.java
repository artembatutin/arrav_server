package net.edge.content.newcombat.attack;

public final class AttackModifier {
    private double accuracy = 1;
    private double aggressive = 1;
    private double defensive = 1;
    private double damage = 1;

    public AttackModifier accuracy(double accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public AttackModifier aggressive(double aggressive) {
        this.aggressive = aggressive;
        return this;
    }

    public AttackModifier defensive(double defensive) {
        this.defensive = defensive;
        return this;
    }

    public AttackModifier damage(double damage) {
        this.damage = damage;
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
