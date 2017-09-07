package net.edge.world.entity.actor.combat.attack;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 7-9-2017.
 */
public final class CurseModifier {

    private double attack;

    private double strength;

    private double defense;

    private double ranged;

    private double magic;

    public CurseModifier() {
        //initialization.
    }

    public double getAttackBonus() {
        return attack;
    }

    public CurseModifier attack(double increase) {
        this.attack += increase;
        return this;
    }

    public double getStrengthBonus() {
        return strength;
    }

    public CurseModifier strength(double increase) {
        this.strength += increase;
        return this;
    }

    public double getDefenseBonus() {
        return defense;
    }

    public CurseModifier defense(double increase) {
        this.defense += increase;
        return this;
    }

    public double getRangedBonus() {
        return ranged;
    }

    public CurseModifier ranged(double increase) {
        this.ranged += increase;
        return this;
    }

    public double getMagicBonus() {
        return magic;
    }

    public CurseModifier magic(double increase) {
        this.magic += increase;
        return this;
    }


}
