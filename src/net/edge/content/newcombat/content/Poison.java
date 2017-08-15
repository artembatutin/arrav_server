package net.edge.content.newcombat.content;

import net.edge.world.entity.actor.Actor;


public class Poison {
    private static final int QUARTER_MINUTE = 25;

    private int strength;
    private int ticks;

    public Poison(int strength) {
        this.strength = strength;
        this.ticks = QUARTER_MINUTE * strength * 4;
    }

    public void sequence(Actor actor) {
        ticks--;

        if (ticks % QUARTER_MINUTE == 0) {
//            actor.damage(new Hit(strength, Hitsplat.POISON, CombatType.MELEE, true));

            if (ticks % 4 == 0) {
                strength--;
            }
        }

        if (ticks == 0) {
            actor.getNewCombat().poison(-1);
        }
    }

    public boolean replace(Poison other) {
        return strength > other.strength;
    }

    public int getTicksRemaining() {
        return ticks;
    }

}