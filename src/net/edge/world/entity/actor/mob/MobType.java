package net.edge.world.entity.actor.mob;

/**
 * Enumeration of several types of Mobs.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum MobType {
	
	/**
	 * None specific.
	 */
	NONE(true),
	
	/*
	 * Godwards soldiers.
	 */
	ARMADYL_SOLDIER(false),
	BANDOS_SOLIDER(false),
	SARADOMIN_SOLDIER(false),
	ZAMORAK_SOLDIER(false),
	
	/*
	 * Hunting
	 */
	HUNTING_MAMMAL(false),
	HUNTING_BIRD(false),
	
	;
	
	private final boolean aggressive;
	
	MobType(boolean aggressive) {
		this.aggressive = aggressive;
	}
	
	public boolean isAggressive() {
		return aggressive;
	}
	
}
