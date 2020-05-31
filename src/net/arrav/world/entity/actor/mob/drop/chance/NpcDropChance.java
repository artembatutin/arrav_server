package net.arrav.world.entity.actor.mob.drop.chance;


import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.actor.player.Player;

/**
 * Handles a players drop chance and takes into account their drop rate
 * boosts. Can also simulate fake boosted drop modifiers.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class NpcDropChance {

    /**
     * The denominator of this NPC drop rate.
     */
    private final int denominator;

    /**
     * Creates a new {@link NpcDropChance}.
     * @param denominator
     *            the denominator of this NPC drop rate.
     */
    public NpcDropChance(int denominator) {
        this.denominator = denominator;
    }

    /**
     * Determines if an NPC drop will be successful or not.
     *
     * the random number generator used to determine this.
     * @return {@code true} if the drop was successful, {@code false} otherwise.
     */
    public boolean test(Player player) {
        int dropRate = player.getDropChanceHandler().getDropRate(false);
        int percent = denominator / 100;
        int random =  (denominator - (percent * (dropRate/ 3)));
        return RandomUtils.success(random);//used to be == o
    }

    /**
     * Determines if an NPC drop will be successful or not. Under false
     * conditions to make the drop table look more appealing.
     *
     * the random number generator used to determine this.
     * @return {@code true} if the drop was successful, {@code false} otherwise.
     */
    public boolean simulate(Player player) {
        int dropRate = player.getDropChanceHandler().getDropRate(true);
        int percent = denominator / 100;
        int random =  (denominator - (percent * (dropRate/ 3)));
        return RandomUtils.success(random);//used to be == o
    }


    /**
     * Gets the denominator of this NPC drop rate.
     *
     * @return the denominator.
     */
    public final double getDenominator() {
        return denominator;
    }

}