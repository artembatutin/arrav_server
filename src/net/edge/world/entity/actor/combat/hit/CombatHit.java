package net.edge.world.entity.actor.combat.hit;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;

import java.util.function.Function;

/**
 * A wrapper for a {@link Hit} object, adding additional variables for hit and
 * hitsplat delays.
 * @author Michael | Chex
 */
public final class CombatHit extends Hit {
	
	/**
	 * The hit delay.
	 */
	private final int hitDelay;
	
	/**
	 * The hitsplat delay.
	 */
	private final int hitsplatDelay;

	/**
	 * Constructs a new {@link CombatHit} object.
	 * @param hit           the hit to wrap
	 * @param hitDelay      the hit delay
	 * @param hitsplatDelay the hitsplat delay
	 */
	public CombatHit(Hit hit, int hitDelay, int hitsplatDelay) {
		super(hit.getDamage(), hit.getHitsplat(), hit.getHitIcon(), hit.isAccurate(), hit.getSource());
		this.setSoak(hit.getSoak());
		this.hitDelay = hitDelay;
		this.hitsplatDelay = hitsplatDelay;
	}
	
	/**
	 * Copies and modifies this combat hit.
	 * @param modifier the damage modification
	 * @return a copy of this combat hit with the damage modifier applied
	 */
	public CombatHit copyAndModify(Actor defender, CombatType type, Function<Integer, Integer> modifier) {
		this.modifyDamage(modifier);
		return new CombatHit(CombatUtil.calculateSoaking(defender, type, this), hitDelay, hitsplatDelay);
	}
	
	/**
	 * Gets the hit delay.
	 * @return the hit delay.
	 */
	public int getHitDelay() {
		return hitDelay;
	}
	
	/**
	 * Gets the hitsplat delay.
	 * @return the hitsplat delay
	 */
	public int getHitsplatDelay() {
		return hitsplatDelay;
	}
	
}
