package net.edge.world.entity.actor.mob;

/**
 * The container that represents an Mob combat definition.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MobDefinitionCombat {
	
	/**
	 * Determines if this NPC is aggressive.
	 */
	private boolean aggressive;
	
	/**
	 * Determines if this NPC retreats.
	 */
	private boolean retreats;
	
	/**
	 * Determines if this NPC is poisonous.
	 */
	private boolean poisonous;
	
	/**
	 * The time it takes for this NPC to respawn.
	 */
	private int respawnTime;
	
	/**
	 * The max hit of this NPC.
	 */
	private int maxHit;
	
	/**
	 * The maximum amount of hitpoints this NPC has.
	 */
	private int hitpoints;
	
	/**
	 * The attack speed of this NPC.
	 */
	private int attackSpeed;
	
	/**
	 * The attack animation of this NPC.
	 */
	private int attackAnimation;
	
	/**
	 * The defence animation of this NPC.
	 */
	private int defenceAnimation;
	
	/**
	 * The death animation of this NPC.
	 */
	private int deathAnimation;
	
	/**
	 * The combat level of this NPC.
	 */
	private int combatLevel;
	
	/**
	 * The attack level of this NPC.
	 */
	private int attackLevel;
	
	/**
	 * The magic level of this NPC.
	 */
	private int magicLevel;
	
	/**
	 * The ranged level of this NPC.
	 */
	private int rangedLevel;
	
	/**
	 * The defence level of this NPC.
	 */
	private int defenceLevel;
	
	/**
	 * The required slayer level to slay this NPC.
	 */
	private int slayerRequirement;
	
	/**
	 * The slayer key of this NPC.
	 */
	private String slayerKey;
	
	/**
	 * The weakness of this NPC.
	 */
	private String weakness;
	
	/**
	 * Creates a new {@link MobDefinitionCombat}.
	 */
	public MobDefinitionCombat(boolean aggressive, boolean retreats, boolean poisonous, int respawnTime, int maxHit, int hitpoints, int attackSpeed, int attackAnimation, int defenceAnimation, int deathAnimation, int combatLevel, int attackLevel, int magicLevel, int rangedLevel, int defenceLevel, int slayerRequirement, String slayerKey, String weakness) {
		this.aggressive = aggressive;
		this.retreats = retreats;
		this.poisonous = poisonous;
		this.respawnTime = respawnTime;
		this.maxHit = maxHit;
		this.hitpoints = hitpoints;
		this.attackSpeed = attackSpeed;
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
	}
	
	/**
	 * Gets the combat level of this npc.
	 * @return the combat level
	 */
	public int getCombatLevel() {
		return combatLevel;
	}
	
	/**
	 * Determines if this NPC is aggressive.
	 * @return {@code true} if this NPC is aggressive, {@code false} otherwise.
	 */
	public boolean aggressive() {
		return aggressive;
	}
	
	/**
	 * Determines if this NPC retreats.
	 * @return {@code true} if this NPC can retreat, {@code false} otherwise.
	 */
	public boolean retreats() {
		return retreats;
	}
	
	/**
	 * Determines if this NPC is poisonous.
	 * @return {@code true} if this NPC is poisonous, {@code false} otherwise.
	 */
	public boolean poisonous() {
		return poisonous;
	}
	
	/**
	 * Gets the time it takes for this NPC to respawn.
	 * @return the respawn time.
	 */
	public int getRespawnTime() {
		return respawnTime <= 0 ? 1 : respawnTime;
	}
	
	/**
	 * Gets the max hit of this NPC.
	 * @return the max hit.
	 */
	public int getMaxHit() {
		return maxHit;
	}
	
	/**
	 * Gets the maximum amount of hitpoints this NPC has.
	 * @return the maximum amount of hitpoints.
	 */
	public int getHitpoints() {
		return hitpoints;
	}
	
	/**
	 * Gets the maximum amount of hitpoints this NPC has.
	 * @return the attack speed.
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	/**
	 * Gets the attack animation of this NPC.
	 * @return the attack animation.
	 */
	public int getAttackAnimation() {
		return attackAnimation;
	}
	
	/**
	 * Gets the defence animation of this NPC.
	 * @return the defence animation.
	 */
	public int getDefenceAnimation() {
		return defenceAnimation;
	}
	
	/**
	 * Gets the death animation of this NPC.
	 * @return the death animation.
	 */
	public int getDeathAnimation() {
		return deathAnimation;
	}
	
	/**
	 * Gets the attack level of this NPC.
	 * @return the attack bonus.
	 */
	public int getAttackLevel() {
		return attackLevel;
	}
	
	/**
	 * Gets the magic level of this NPC.
	 * @return the melee defence bonus.
	 */
	public int getMagicLevel() {
		return magicLevel;
	}
	
	/**
	 * Gets the ranged level of this NPC.
	 * @return the ranged defence bonus.
	 */
	public int getRangedLevel() {
		return rangedLevel;
	}
	
	/**
	 * Gets the defence level of this NPC.
	 * @return the magic defence bonus.
	 */
	public int getDefenceLevel() {
		return defenceLevel;
	}
	
	/**
	 * Gets the slayer requirement level.
	 * @return slayer requirement level.
	 */
	public int getSlayerRequirement() {
		return slayerRequirement;
	}
	
	/**
	 * Gets the slayer key.
	 * @return slayer key.
	 */
	public String getSlayerKey() {
		return slayerKey;
	}
	
	/**
	 * Gets the combat weakness.
	 * @return combat weakness.
	 */
	public String getWeakness() {
		return weakness;
	}
}
