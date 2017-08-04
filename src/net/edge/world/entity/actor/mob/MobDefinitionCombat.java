package net.edge.world.entity.actor.mob;

/**
 * The container that represents an Mob combat definition.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MobDefinitionCombat {
	
	/**
	 * Determines if this NPC is aggressive.
	 */
	public boolean aggressive;
	
	/**
	 * Determines if this NPC retreats.
	 */
	public boolean retreats;
	
	/**
	 * Determines if this NPC is poisonous.
	 */
	public boolean poisonous;
	
	/**
	 * The time it takes for this NPC to respawn.
	 */
	public int respawnTime;
	
	/**
	 * The max hit of this mob.
	 */
	public int maxHit;
	
	/**
	 * The maximum amount of hitpoints this NPC has.
	 */
	public int hitpoints;
	
	/**
	 * The attack speed of this mob.
	 */
	public int attackDelay;
	
	/**
	 * The attack animation of this mob.
	 */
	public int attackAnimation;
	
	/**
	 * The defence animation of this mob.
	 */
	public int defenceAnimation;
	
	/**
	 * The death animation of this mob.
	 */
	public int deathAnimation;
	
	/**
	 * The combat level of this mob.
	 */
	public int combatLevel;
	
	/**
	 * The attack level of this mob.
	 */
	public int attackLevel;
	
	/**
	 * The magic level of this mob.
	 */
	public int magicLevel;
	
	/**
	 * The ranged level of this mob.
	 */
	public int rangedLevel;
	
	/**
	 * The defence level of this mob.
	 */
	public int defenceLevel;
	
	/**
	 * The strength level of this mob.
	 */
	public int strengthLevel;
	
	/**
	 * The attacking melee attribute.
	 */
	public int attackMelee;
	
	/**
	 * The attacking magic attribute.
	 */
	public int attackMagic;
	
	/**
	 * The attacking ranged attribute.
	 */
	public int attackRanged;
	
	/**
	 * The defending stab attribute.
	 */
	public int defenceStab;
	
	/**
	 * The defending slash attribute.
	 */
	public int defenceSlash;
	
	/**
	 * The defending crush attribute.
	 */
	public int defenceCrush;
	
	/**
	 * The defending magic attribute.
	 */
	public int defenceMagic;
	
	/**
	 * The defending defence attribute.
	 */
	public int defenceRanged;
	
	/**
	 * The required slayer level to slay this NPC.
	 */
	public int slayerRequirement;
	
	/**
	 * The slayer key of this mob.
	 */
	public String slayerKey;
	
	/**
	 * The weakness of this mob.
	 */
	public String weakness;
	
	/**
	 * Creates a new {@link MobDefinitionCombat}.
	 */
	public MobDefinitionCombat(boolean aggressive, boolean retreats, boolean poisonous, int respawnTime, int maxHit, int hitpoints, int attackDelay, int attackAnimation, int defenceAnimation, int deathAnimation, int combatLevel, int attackLevel, int magicLevel, int rangedLevel, int defenceLevel, int slayerRequirement, String slayerKey, String weakness, int attackMelee, int attackMagic, int attackRanged, int defenceStab, int defenceSlash, int defenceCrush, int defenceMagic, int defenceRanged) {
		this.aggressive = aggressive;
		this.retreats = retreats;
		this.poisonous = poisonous;
		this.respawnTime = respawnTime;
		this.maxHit = maxHit;
		this.hitpoints = hitpoints;
		this.attackDelay = attackDelay;
		this.attackAnimation = attackAnimation;
		this.defenceAnimation = defenceAnimation;
		this.deathAnimation = deathAnimation;
		this.combatLevel = combatLevel;
		this.attackLevel = attackLevel;
		this.magicLevel = magicLevel;
		this.rangedLevel = rangedLevel;
		this.defenceLevel = defenceLevel;
		this.slayerRequirement = slayerRequirement;
		this.slayerKey = slayerKey;
		this.weakness = weakness;
		this.attackMelee = attackMelee;
		this.attackMagic = attackMagic;
		this.attackRanged = attackRanged;
		this.defenceStab = defenceStab;
		this.defenceSlash = defenceSlash;
		this.defenceCrush = defenceCrush;
		this.defenceMagic = defenceMagic;
		this.defenceRanged = defenceRanged;
	}
	
	public int getCombatLevel() {
		return combatLevel;
	}
	
	public boolean aggressive() {
		return aggressive;
	}
	
	public boolean retreats() {
		return retreats;
	}
	
	public boolean poisonous() {
		return poisonous;
	}
	
	public int getRespawnTime() {
		return respawnTime <= 0 ? 1 : respawnTime;
	}
	
	public int getMaxHit() {
		return maxHit;
	}
	
	public int getHitpoints() {
		return hitpoints;
	}
	
	public int getAttackDelay() {
		return attackDelay;
	}
	
	public int getAttackAnimation() {
		return attackAnimation;
	}
	
	public int getDefenceAnimation() {
		return defenceAnimation;
	}
	
	public int getDeathAnimation() {
		return deathAnimation;
	}
	
	public int getAttackLevel() {
		return attackLevel;
	}
	
	public int getMagicLevel() {
		return magicLevel;
	}
	
	public int getRangedLevel() {
		return rangedLevel;
	}
	
	public int getStrengthLevel() {
		return strengthLevel;
	}
	
	public int getDefenceLevel() {
		return defenceLevel;
	}
	
	public int getAttackMelee() {
		return attackMelee;
	}
	
	public int getAttackMagic() {
		return attackMagic;
	}
	
	public int getAttackRanged() {
		return attackRanged;
	}
	
	public int getDefenceStab() {
		return defenceStab;
	}
	
	public int getDefenceSlash() {
		return defenceSlash;
	}
	
	public int getDefenceCrush() {
		return defenceCrush;
	}
	
	public int getDefenceMagic() {
		return defenceMagic;
	}
	
	public int getDefenceRanged() {
		return defenceRanged;
	}
	
	public int getSlayerRequirement() {
		return slayerRequirement;
	}
	
	public String getSlayerKey() {
		return slayerKey;
	}
	
	public String getWeakness() {
		return weakness;
	}
}
