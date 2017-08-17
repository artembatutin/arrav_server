package net.edge.content.newcombat.hit;

import java.util.function.Function;

/**
 * A {@code Hit} object holds the damage amount and hitsplat data.
 *
 * @author Michael | Chex
 */
public class Hit {

    /** The damage amount. */
    private int damage;

    /** The hitsplat type. */
    private Hitsplat hitsplat;

    /** The hit icon. */
    private HitIcon hitIcon;

    /** Whether or not this hit is accurate. */
    private final boolean accurate;

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param damage   the damage amount
     * @param hitsplat the hitsplat type
     * @param hitIcon  the hit icon
     * @param accurate whether or not this hit is accurate
     */
    public Hit(int damage, Hitsplat hitsplat, HitIcon hitIcon, boolean accurate) {
        this.damage = damage;
        this.hitsplat = hitsplat;
        this.hitIcon = hitIcon;
        this.accurate = accurate;
    }

    /**
     * Sets the hit damage.
     *
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Sets the hit damage with a function. If the damage is less than one, the
     * damage is set to zero and hitsplat set to block.
     *
     * @param modifier the modifier to damage
     */
    public void modifyDamage(Function<Integer, Integer> modifier) {
        damage = modifier.apply(damage);

        if (damage <= 0) {
            damage = 0;
            hitsplat = Hitsplat.NORMAL;
        }
    }

    /**
     * Gets the damage amount.
     *
     * @return the damage amount
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the damage type.
     *
     * @return the damage type
     */
    public Hitsplat getHitsplat() {
        return hitsplat;
    }

    /**
     * Gets the hit icon.
     *
     * @return the hit icon
     */
    public HitIcon getHitIcon() {
        return hitIcon;
    }

    /**
     * Checks if the hit is accurate.
     *
     * @return {@code true} if the hit is accurate
     */
    public boolean isAccurate() {
        return accurate;
    }

}
