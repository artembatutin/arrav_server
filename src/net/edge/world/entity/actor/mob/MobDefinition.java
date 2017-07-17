package net.edge.world.entity.actor.mob;

import net.edge.util.json.JsonSaver;

import java.util.Arrays;
import java.util.Optional;

/**
 * The container that represents an NPC definition.
 * @author lare96 <http://github.com/lare96>
 */
public final class MobDefinition {
	
	/**
	 * The array that contains all of the NPC definitions.
	 */
	public static final MobDefinition[] DEFINITIONS = new MobDefinition[16733];
	
	/**
	 * The identification for this NPC.
	 */
	private final int id;
	
	/**
	 * The name of this NPC.
	 */
	private final String name;
	
	/**
	 * The description of this NPC.
	 */
	private final String description;
	
	/**
	 * The size of this NPC.
	 */
	private final int size;
	
	/**
	 * Determines if this NPC can be attacked.
	 */
	private final boolean attackable;
	
	/**
	 * Determines if this NPC is aggressive.
	 */
	private final boolean aggressive;
	
	/**
	 * Determines if this NPC retreats.
	 */
	private final boolean retreats;
	
	/**
	 * Determines if this NPC is poisonous.
	 */
	private final boolean poisonous;
	
	/**
	 * The time it takes for this NPC to respawn.
	 */
	private final int respawnTime;
	
	/**
	 * The max hit of this NPC.
	 */
	private final int maxHit;
	
	/**
	 * The maximum amount of hitpoints this NPC has.
	 */
	private final int hitpoints;
	
	/**
	 * The attack speed of this NPC.
	 */
	private final int attackSpeed;
	
	/**
	 * The attack animation of this NPC.
	 */
	private final int attackAnimation;
	
	/**
	 * The defence animation of this NPC.
	 */
	private final int defenceAnimation;
	
	/**
	 * The death animation of this NPC.
	 */
	private final int deathAnimation;
	
	/**
	 * The combat level of this NPC.
	 */
	private final int combatLevel;
	
	/**
	 * The attack level of this NPC.
	 */
	private final int attackLevel;
	
	/**
	 * The magic level of this NPC.
	 */
	private final int magicLevel;
	
	/**
	 * The ranged level of this NPC.
	 */
	private final int rangedLevel;
	
	/**
	 * The defence level of this NPC.
	 */
	private final int defenceLevel;
	
	/**
	 * The required slayer level to slay this NPC.
	 */
	private final int slayerRequirement;
	
	/**
	 * The slayer key of this NPC.
	 */
	private final String slayerKey;
	
	/**
	 * The weakness of this NPC.
	 */
	private final String weakness;
	
	/**
	 * Creates a new {@link MobDefinition}.
	 * @param id                the identification for this NPC.
	 * @param name              the name of this NPC.
	 * @param description       the description of this NPC.
	 * @param combatLevel       the combat level of this NPC.
	 * @param size              the size of this NPC.
	 * @param attackable        determines if this NPC can be attacked.
	 * @param aggressive        determines if this NPC is aggressive.
	 * @param retreats          determines if this NPC retreats.
	 * @param poisonous         determines if this NPC is poisonous.
	 * @param respawnTime       the time it takes for this NPC to respawn.
	 * @param maxHit            the max hit of this NPC.
	 * @param hitpoints         the maximum amount of hitpoints this NPC has.
	 * @param attackSpeed       the attack speed of this NPC.
	 * @param attackAnimation   the attack animation of this NPC.
	 * @param defenceAnimation  the defence animation of this NPC.
	 * @param deathAnimation    the death animation of this NPC.
	 * @param attackLevel       the attack level of this NPC.
	 * @param magicLevel        the magic level of this NPC.
	 * @param rangedLevel       the ranged level of this NPC.
	 * @param defenceLevel      the defence level of this NPC.
	 * @param slayerRequirement the requirement slayer level to slay this NPC.
	 * @param slayerKey         The slayer key of this NPC.
	 * @param weakness          the weakness of this NPC.
	 */
	public MobDefinition(int id, String name, String description, int combatLevel, int size, boolean attackable, boolean aggressive, boolean retreats, boolean poisonous, int respawnTime, int maxHit, int hitpoints, int attackSpeed, int attackAnimation, int defenceAnimation, int deathAnimation, int attackLevel, int magicLevel, int rangedLevel, int defenceLevel, int slayerRequirement, String slayerKey, String weakness) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.combatLevel = combatLevel;
		this.size = size;
		this.attackable = attackable;
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
		this.attackLevel = attackLevel;
		this.magicLevel = magicLevel;
		this.rangedLevel = rangedLevel;
		this.defenceLevel = defenceLevel;
		this.slayerRequirement = slayerRequirement;
		this.slayerKey = slayerKey;
		this.weakness = weakness;
	}
	
	public static Optional<MobDefinition> fromString(String name) {
		return Arrays.stream(DEFINITIONS).filter($it -> $it.getName().equalsIgnoreCase(name)).findAny();
	}
	
	public static Optional<MobDefinition> fromSlayerKey(String key) {
		return Arrays.stream(DEFINITIONS).filter($it -> $it.getSlayerKey().equalsIgnoreCase(key)).findAny();
	}
	
	/**
	 * Gets the identification for this NPC.
	 * @return the identification.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name of this NPC.
	 * @return the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description of this NPC.
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the combat level of this npc.
	 * @return the combat level
	 */
	public int getCombatLevel() {
		return combatLevel;
	}
	
	/**
	 * Gets the size of this NPC.
	 * @return the size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Determines if this NPC can be attacked.
	 * @return {@code true} if this NPC can be attacked, {@code false}
	 * otherwise.
	 */
	public boolean isAttackable() {
		return attackable;
	}
	
	/**
	 * Determines if this NPC is aggressive.
	 * @return {@code true} if this NPC is aggressive, {@code false} otherwise.
	 */
	public boolean isAggressive() {
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
	public boolean isPoisonous() {
		return poisonous;
	}
	
	/**
	 * Gets the time it takes for this NPC to respawn.
	 * @return the respawn time.
	 */
	public int getRespawnTime() {
		return ((respawnTime - 1) <= 0 ? 1 : (respawnTime - 1));
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
		return hitpoints * 10;
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
	
	public int getSlayerRequirement() {
		return slayerRequirement;
	}
	
	public String getSlayerKey() {
		return slayerKey;
	}
	
	public String getWeakness() {
		return weakness;
	}
	
	/**
	 * Dumps all the definitions to a new json file.
	 */
	public static void dump() {
		JsonSaver json = new JsonSaver();
		for(MobDefinition d : MobDefinition.DEFINITIONS) {
			json.current().addProperty("id", d.id);
			json.current().addProperty("name", d.name);
			json.current().addProperty("size", d.size);
			json.current().addProperty("description", d.description);
			json.current().addProperty("respawn", d.respawnTime);
			json.current().addProperty("hitpoints", d.hitpoints);
			json.current().addProperty("maxHit", d.maxHit);
			json.current().addProperty("attackSpeed", d.attackSpeed);
			json.current().addProperty("combatLevel", d.combatLevel);
			json.current().addProperty("attackLevel", d.attackLevel);
			json.current().addProperty("magicLevel", d.magicLevel);
			json.current().addProperty("rangedLevel", d.rangedLevel);
			json.current().addProperty("defenceLevel", d.defenceLevel);
			json.current().addProperty("attackable", d.attackable);
			json.current().addProperty("aggressive", d.aggressive);
			json.current().addProperty("retreats", d.retreats);
			json.current().addProperty("poisonous", d.poisonous);
			json.current().addProperty("attackAnim", d.attackAnimation);
			json.current().addProperty("defenceAnim", d.defenceAnimation);
			json.current().addProperty("deathAnim", d.deathAnimation);
			json.current().addProperty("slayerRequirement", d.slayerRequirement);
			json.current().addProperty("slayerKey", d.slayerKey);
			json.current().addProperty("weakness", d.weakness);
			json.split();
		}
		json.publish("./data/dumps/new_npc.json");
	}
	
}
