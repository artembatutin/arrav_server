package net.edge.world.entity.actor.mob;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatType;
import net.edge.util.json.JsonSaver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Optional;

/**
 * The container that represents an Mob definition.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobDefinition {
	
	/**
	 * A dummy {@link MobDefinitionCombat} to prevent nullpointers.
	 */
	public static final MobDefinitionCombat NON_COMBAT = new MobDefinitionCombat(false, true, false, 10, 10, 10, 7, -1, -1, -1, 3, 3, 3, 3, 3, 3, "", "", 0, 0, 0, 0, 0, 0, 0, 0);
	
	/**
	 * The array that contains all of the NPC definitions.
	 */
	public static final MobDefinition[] DEFINITIONS = new MobDefinition[16733];
	
	/**
	 * The identification for this NPC.
	 */
	private int id;
	
	/**
	 * The name of this NPC.
	 */
	private String name;
	
	/**
	 * The description of this NPC.
	 */
	private String description;
	
	/**
	 * The size of this NPC.
	 */
	private int size;
	
	/**
	 * Determines if this NPC can be attacked.
	 */
	private boolean attackable;
	
	/**
	 * The mob's combat definition.
	 */
	private MobDefinitionCombat combat;

	/**
	 * The combat attack data.
	 */
	private CombatAttackData attackData;

	/**
	 * Creates a new {@link MobDefinition}.
	 */
	public MobDefinition(int id, String name, String description, int size, boolean attackable, MobDefinitionCombat combat, CombatAttackData attackData) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.size = size;
		this.attackable = attackable;
		this.combat = combat;
		this.attackData = attackData;
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
	 * Gets the combat level of this npc.
	 * @return the combat level
	 */
	public int getCombatLevel() {
		return combat == null ? NON_COMBAT.getCombatLevel() : combat.getCombatLevel();
	}
	
	/**
	 * Determines if this NPC is aggressive.
	 * @return {@code true} if this NPC is aggressive, {@code false} otherwise.
	 */
	public boolean aggressive() {
		return combat == null ? NON_COMBAT.aggressive() : combat.aggressive();
	}
	
	/**
	 * Determines if this NPC retreats.
	 * @return {@code true} if this NPC can retreat, {@code false} otherwise.
	 */
	public boolean retreats() {
		return combat == null ? NON_COMBAT.retreats() : combat.retreats();
	}
	
	/**
	 * Determines if this NPC is poisonous.
	 * @return {@code true} if this NPC is poisonous, {@code false} otherwise.
	 */
	public boolean poisonous() {
		return combat == null ? NON_COMBAT.poisonous() : combat.poisonous();
	}
	
	/**
	 * Gets the time it takes for this NPC to respawn.
	 * @return the respawn time.
	 */
	public int getRespawnTime() {
		int respawnTime = combat == null ? NON_COMBAT.getRespawnTime() : combat.getRespawnTime();
		return respawnTime <= 0 ? 1 : respawnTime;
	}
	
	/**
	 * Gets the max hit of this NPC.
	 * @return the max hit.
	 */
	public int getMaxHit() {
		return combat == null ? NON_COMBAT.getMaxHit() : combat.getMaxHit();
	}
	
	/**
	 * Gets the maximum amount of hitpoints this NPC has.
	 * @return the maximum amount of hitpoints.
	 */
	public int getHitpoints() {
		return combat == null ? NON_COMBAT.getHitpoints() : combat.getHitpoints();
	}
	
	/**
	 * Gets the maximum amount of hitpoints this NPC has.
	 * @return the attack speed.
	 */
	public int getAttackDelay() {
		return combat == null ? NON_COMBAT.getAttackDelay() : combat.getAttackDelay();
	}
	
	/**
	 * Gets the attack animation of this NPC.
	 * @return the attack animation.
	 */
	public int getAttackAnimation() {
		return combat == null ? NON_COMBAT.getAttackAnimation() : combat.getAttackAnimation();
	}
	
	/**
	 * Gets the defence animation of this NPC.
	 * @return the defence animation.
	 */
	public int getDefenceAnimation() {
		return combat == null ? NON_COMBAT.getDefenceAnimation() : combat.getDefenceAnimation();
	}
	
	/**
	 * Gets the death animation of this NPC.
	 * @return the death animation.
	 */
	public int getDeathAnimation() {
		return combat == null ? NON_COMBAT.getDeathAnimation() : combat.getDeathAnimation();
	}
	
	/**
	 * Gets the attack level of this NPC.
	 * @return the attack bonus.
	 */
	public int getAttackLevel() {
		return combat == null ? NON_COMBAT.getAttackLevel() : combat.getAttackLevel();
	}
	
	/**
	 * Gets the magic level of this NPC.
	 * @return the melee defence bonus.
	 */
	public int getMagicLevel() {
		return combat == null ? NON_COMBAT.getMagicLevel() : combat.getMagicLevel();
	}
	
	/**
	 * Gets the ranged level of this NPC.
	 * @return the ranged defence bonus.
	 */
	public int getRangedLevel() {
		return combat == null ? NON_COMBAT.getRangedLevel() : combat.getRangedLevel();
	}
	
	/**
	 * Gets the defence level of this NPC.
	 * @return the magic defence bonus.
	 */
	public int getDefenceLevel() {
		return combat == null ? NON_COMBAT.getDefenceLevel() : combat.getDefenceLevel();
	}
	
	/**
	 * Gets the slayer requirement level.
	 * @return slayer requirement level.
	 */
	public int getSlayerRequirement() {
		return combat == null ? NON_COMBAT.getSlayerRequirement() : combat.getSlayerRequirement();
	}
	
	/**
	 * Gets the slayer key.
	 * @return slayer key.
	 */
	public String getSlayerKey() {
		return combat == null ? NON_COMBAT.getSlayerKey() : combat.getSlayerKey();
	}
	
	/**
	 * Gets the combat weakness.
	 * @return combat weakness.
	 */
	public String getWeakness() {
		return combat == null ? NON_COMBAT.getWeakness() : combat.getWeakness();
	}
	
	/**
	 * Gets the extended combat definition.
	 * @return combat.
	 */
	public MobDefinitionCombat getCombat() {
		return combat == null ? NON_COMBAT : combat;
	}

	public Optional<CombatAttackData> getCombatAttackData() {
		return Optional.ofNullable(attackData);
	}

	/**
	 * Dumps all the definitions to a new json file.
	 */
	public static void dump() {
		JsonSaver json = new JsonSaver();
		for(MobDefinition d : MobDefinition.DEFINITIONS) {
			//link(d, d.getName());
			json.current().addProperty("id", d.id);
			json.current().addProperty("name", d.name);
			json.current().addProperty("size", d.size);
			json.current().addProperty("description", d.description);
			json.current().addProperty("attackable", d.attackable);
			if(d.attackable) {
				//process
				json.current().addProperty("respawn", d.combat.getRespawnTime());
				json.current().addProperty("hitpoints", d.combat.getHitpoints());
				json.current().addProperty("maxHit", d.combat.getMaxHit());
				json.current().addProperty("attackDelay", d.combat.getAttackDelay());
				//levels
				json.current().addProperty("combatLevel", d.combat.getCombatLevel());
				json.current().addProperty("attackLevel", d.getAttackLevel());
				json.current().addProperty("magicLevel", d.combat.getMagicLevel());
				json.current().addProperty("rangedLevel", d.combat.getRangedLevel());
				json.current().addProperty("defenceLevel", d.combat.getDefenceLevel());
				json.current().addProperty("strengthLevel", d.combat.getStrengthLevel());
				//attacks
				json.current().addProperty("atkMelee", d.combat.getAttackMelee());
				json.current().addProperty("atkMagic", d.combat.getAttackMagic());
				json.current().addProperty("atkRange", d.combat.getAttackRanged());
				//defence
				json.current().addProperty("defStab", d.combat.getDefenceStab());
				json.current().addProperty("defSlash", d.combat.getDefenceSlash());
				json.current().addProperty("defCrush", d.combat.getDefenceCrush());
				json.current().addProperty("defMagic", d.combat.getDefenceMagic());
				json.current().addProperty("defRange", d.combat.getDefenceRanged());
				//flags
				json.current().addProperty("aggressive", d.combat.aggressive());
				json.current().addProperty("retreats", d.combat.retreats());
				json.current().addProperty("poisonous", d.combat.poisonous());
				//anims
				json.current().addProperty("attackAnim", d.combat.getAttackAnimation());
				json.current().addProperty("defenceAnim", d.combat.getDefenceAnimation());
				json.current().addProperty("deathAnim", d.combat.getDeathAnimation());
				//slayer
				json.current().addProperty("slayerRequirement", d.combat.getSlayerRequirement());
				json.current().addProperty("slayerKey", d.combat.getSlayerKey());
				json.current().addProperty("weakness", d.combat.getWeakness());
			}
			json.split();
		}
		json.publish("./data/def/mob/new_mob.json");
	}
	
	private static void link(MobDefinition d, String name) {
		if(!d.attackable) {
			return;
		}
		String url = "http://oldschoolrunescape.wikia.com/wiki/" + name + "?action=raw";
		//String url = "http://services.runescape.com/m=itemdb_rs/bestiary/beastData.json?beastid=" + d.getId();
		try {
			parseFrom07(url, d);
		} catch (Exception e) {
			if(!(e instanceof FileNotFoundException))
				e.printStackTrace();
			System.out.println("(" + d.id + ") Failed to parse " + url + ", msg: " + e);
		}
	}
	
	private static void bestiary(String url, MobDefinition def) throws Exception {
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		if(def == null || def.getName() == null || def.getCombat() == null)
			return;
		try(BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(in);
			if(el.isJsonNull())
				return;
			JsonObject obj = el.getAsJsonObject();
			String name = obj.get("name").getAsString();
			if(def.getName().toLowerCase().equals(name.toLowerCase())) {
				def.getCombat().aggressive = obj.get("aggressive").getAsBoolean();
				def.getCombat().poisonous = obj.get("poisonous").getAsBoolean();
				JsonObject anims = (JsonObject) obj.get("animations");
				boolean combatible = false;
				if(anims.has("death")) {
					def.getCombat().deathAnimation = anims.get("death").getAsInt();
					combatible = true;
				}
				if(anims.has("attack")) {
					def.getCombat().attackAnimation = anims.get("attack").getAsInt();
					combatible = true;
				}
				if(anims.has("magic") && def.getCombat().getMagicLevel() <= 0) {
					def.getCombat().magicLevel = obj.get("magic").getAsInt();
					combatible = true;
				}
				if(anims.has("defence") && def.getCombat().getDefenceLevel() <= 0) {
					def.getCombat().defenceLevel = obj.get("defence").getAsInt();
					combatible = true;
				}
				if(anims.has("ranged") && def.getCombat().getRangedLevel() <= 0) {
					def.getCombat().rangedLevel = obj.get("ranged").getAsInt();
					combatible = true;
				}
				if(anims.has("attack") && def.getCombat().getAttackLevel() <= 0) {
					def.getCombat().attackLevel = obj.get("attack").getAsInt();
					combatible = true;
				}
				if(!combatible) {
					def.combat = null;
					def.attackable = false;
					System.out.println(def.getId() + " - " + def.getName() + " is not compatible");
				} else {
					System.out.println(def.getId() + " - " + def.getName() + " updated");
				}
			}
		}
	}
	
	private static void parseFrom07(String url, MobDefinition def) throws Exception {
		if(url.contains(" ")) {
			url = url.replaceAll(" ", "%20");
		}
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
			String inputLine;
			int cmb = 0;
			boolean changed = false;
			while ((inputLine = in.readLine()) != null) {
				if(inputLine.contains("**[[") && inputLine.contains("Common")) {
					System.out.println("Common: " + def.getName());
					link(def, def.getName()+"_(Common)");
					return;
				}
				if (inputLine.startsWith("|")) {
					String[] split = inputLine.substring(1, inputLine.length()).split("=");
					if (split.length != 2) {
						continue;
					}
					
					String key = split[0].replaceAll("}", " ").trim();
					String value = split[1].replaceAll("}", " ").trim();
					if (value.contains(",")) {
						value = value.split(",")[0];
					}
					switch (key) {
						case "combat":
							if(cmb != 0)
								changed = true;
							cmb = parseInt(value);
							break;
						case "attbns":
							if(def.getCombatLevel() == cmb || !changed)
								def.combat.attackMelee = parseInt(value);
							break;
						case "amagic":
							if(def.getCombatLevel() == cmb || !changed)
								def.combat.attackMagic = parseInt(value);
							break;
						case "arange":
							if(def.getCombatLevel() == cmb || !changed)
								def.combat.attackRanged = parseInt(value);
							break;
						/*case "str":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.strengthLevel = parseInt(value);
							break;
						case "def":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.defenceLevel = parseInt(value);
							break;
						case "range":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.rangedLevel = parseInt(value);
							break;
						case "mage":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.magicLevel = parseInt(value);
							break;
						case "max hit":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.maxHit = parseInt(value) * 10;
							break;
						
						case "dstab":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.defenceStab = parseInt(value);
							break;
						case "dslash":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.defenceSlash = parseInt(value);
							break;
						case "dcrush":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.defenceCrush = parseInt(value);
							break;
						case "dmagic":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.defenceMagic = parseInt(value);
							break;
						case "drange":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.defenceRanged = parseInt(value);
							break;
						
						
						case "hp":
						case "hitpoints":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.hitpoints = parseInt(value);
							break;
						case "attack speed":
							if(def.combat != null && def.getCombatLevel() == cmb)
								def.combat.attackDelay = (10 - parseInt(value));
							break;*/
					}
				}
			}
			System.out.println(def.getId() + " - " + def.getName() + (" - melee: " + def.getCombat().attackMelee)+ (" - range: " + def.getCombat().attackRanged)+ (" - magic: " + def.getCombat().attackMagic));
		}
	}
	
	private static int parseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getStrengthLevel() {
		return combat.strengthLevel;
	}

	public static final class CombatAttackData {

		public final CombatType type;

		public final String key;

		public CombatAttackData(CombatType type, String key) {
			this.type = type;
			this.key = key;
		}

		public CombatProjectileDefinition getDefinition() {
			return CombatProjectileDefinition.getDefinition(key);
		}
	}

}
