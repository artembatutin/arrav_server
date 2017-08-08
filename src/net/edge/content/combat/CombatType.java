package net.edge.content.combat;

import net.edge.world.Hit;

/**
 * The enumerated type whose elements represent the different types of combat.
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatType {
	
	/**
	 * The melee combat type, includes things like swords and daggers.
	 */
	MELEE(Hit.HitIcon.MELEE),
	
	/**
	 * The ranged combat type, includes things like bows and crossbows.
	 */
	RANGED(Hit.HitIcon.RANGED),
	
	/**
	 * The magic combat type, includes things like fire blast and ice barrage.
	 */
	MAGIC(Hit.HitIcon.MAGIC),
	
	/**
	 * Represents no combat type.
	 */
	NONE(Hit.HitIcon.NONE);
	
	private final Hit.HitIcon icon;
	
	CombatType(Hit.HitIcon icon) {
		this.icon = icon;
	}
	
	public Hit.HitIcon getIcon() {
		return icon;
	}
	
}