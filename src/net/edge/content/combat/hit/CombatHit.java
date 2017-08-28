package net.edge.content.combat.hit;

import java.util.function.Function;

/**
 * A wrapper for a {@link Hit} object, adding additional variables for hit and
 * hitsplat delays.
 *
 * @author Michael | Chex
 */
public final class CombatHit extends Hit {

    /** The hit delay. */
    private final int hitDelay;

    /** The hitsplat delay. */
    private final int hitsplatDelay;

    /**
     * Constructs a new {@link CombatHit} object.
     *
     * @param hit           the hit to wrap
     * @param hitDelay      the hit delay
     * @param hitsplatDelay the hitsplat delay
     */
    public CombatHit(Hit hit, int hitDelay, int hitsplatDelay) {
        super(hit.getDamage(), hit.getHitsplat(), hit.getHitIcon(), hit.isAccurate());
        this.hitDelay = hitDelay;
        this.hitsplatDelay = hitsplatDelay;
    }

    /**
     * Copies and modifies this combat hit.
     *
     * @param modifier the damage modification
     * @return a copy of this combat hit with the damage modifier applied
     */
    public CombatHit copyAndModify(Function<Integer, Integer> modifier) {
        CombatHit next = new CombatHit(this, hitDelay, hitsplatDelay);
        next.modifyDamage(modifier);
        return next;
    }

    /**
     * Gets the hit delay.
     *
     * @return the hit delay.
     */
    public int getHitDelay() {
        return hitDelay;
    }

    /**
     * Gets the hitsplat delay.
     *
     * @return the hitsplat delay
     */
    public int getHitsplatDelay() {
        return hitsplatDelay;
    }

}
