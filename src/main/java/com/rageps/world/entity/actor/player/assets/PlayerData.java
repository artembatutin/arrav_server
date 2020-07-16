package com.rageps.world.entity.actor.player.assets;

import com.rageps.util.MutableNumber;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PlayerData {

    public PlayerData() {

    }

    /**
     * The collection of counters used for various counting operations.
     */
    private final MutableNumber poisonImmunity = new MutableNumber(), teleblockTimer = new MutableNumber(), skullTimer = new MutableNumber(), specialPercentage = new MutableNumber(100);

    /**
     * Holds an optional wrapped inside the Antifire details.
     */
    private Optional<AntifireDetails> antifireDetails = Optional.empty();

    public double runEnergy = 100D;



    /**
     * Gets the teleblock counter value.
     * @return the teleblock counter.
     */
    public MutableNumber getTeleblockTimer() {
        return teleblockTimer;
    }

    /**
     * Gets the skull timer counter value.
     * @return the skull timer counter.
     */
    public MutableNumber getSkullTimer() {
        return skullTimer;
    }

    public double getRunEnergy() {
        return runEnergy;
    }

    /**
     * Gets the special percentage counter value.
     * @return the special percentage counter.
     */
    public MutableNumber getSpecialPercentage() {
        return specialPercentage;
    }
    /**
     * Gets the poison immunity counter value.
     * @return the poison immunity counter.
     */
    public MutableNumber getPoisonImmunity() {
        return poisonImmunity;
    }

    /**
     * Gets the anti-fire details instance for this player.
     * @return the {@link AntifireDetails} as an optional.
     */
    public Optional<AntifireDetails> getAntifireDetails() {
        return antifireDetails;
    }

    /**
     * Sets a new anti-fire instance for this class.
     * @param details the anti-fire instance to set.
     */
    public void setAntifireDetail(Optional<AntifireDetails> details) {
        this.antifireDetails = details;
    }

    /**
     * Sets the new anti-fire instance for this class directly.
     * @param details the anti-fire instance to set.
     */
    public void setAntifireDetail(AntifireDetails details) {
        setAntifireDetail(details == null ? Optional.empty() : Optional.of(details));
    }



}
