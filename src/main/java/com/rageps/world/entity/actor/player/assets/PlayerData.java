package com.rageps.world.entity.actor.player.assets;

import com.rageps.content.skill.magic.Spellbook;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.content.skill.prayer.PrayerBook;
import com.rageps.content.skill.slayer.Slayer;
import com.rageps.util.MutableNumber;
import com.rageps.world.entity.actor.mob.Mob;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.ArrayList;
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
    public int ringOfRecoil = 400;

    public boolean venged, lockedXP;

    /**
     * The array determining which quick prayers id are stored.
     */
    public ArrayList<Prayer> quickPrayers = new ArrayList<>();

    /**
     * Holds an optional wrapped inside the Antifire details.
     */
    private Optional<AntifireDetails> antifireDetails = Optional.empty();

    public double runEnergy = 100D;
    /**
     * A list containing all the blocked slayer tasks of the player.
     */
    public String[] blockedTasks = new String[5];

    /**
     * The slayer instance for this player.
     */
    public Optional<Slayer> slayer = Optional.empty();

    /**
     * The current prayer type used by the player.
     */
    public PrayerBook prayerBook = PrayerBook.NORMAL;

    /**
     * The current spellbook used by the player.
     */
    public Spellbook spellbook = Spellbook.NORMAL;

    /**
     * The total amount of npcs this player has killed.
     */
    public final MutableNumber npcKills = new MutableNumber();

    /**
     * The total amount of times this player died to a {@link Mob}
     */
    public final MutableNumber npcDeaths = new MutableNumber();

    public int[] godwarsKillcount = new int[4];

    /**
     * The amount of pest points the player has.
     */
    public int pestPoints;

    /**
     * The amount of slayer points the player has.
     */
    public int slayerPoints;

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
