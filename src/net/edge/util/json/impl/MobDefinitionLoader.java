package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.skill.slayer.Slayer;
import net.edge.world.entity.actor.mob.MobAggression;
import net.edge.world.entity.actor.mob.MobDefinition;
import net.edge.world.entity.actor.mob.MobDefinitionCombat;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all npc definitions.
 * @author lare96 <http://github.com/lare96>
 */
public final class MobDefinitionLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link MobDefinitionLoader}.
	 */
	public MobDefinitionLoader() {
		super("./data/def/mob/mob_definitions.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int index = reader.get("id").getAsInt();
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		String description = Objects.requireNonNull(reader.get("description").getAsString());
		int size = reader.get("size").getAsInt();
		boolean attackable = reader.get("attackable").getAsBoolean();
		MobDefinitionCombat combat = null;
		if(attackable) {
			boolean aggressive = reader.get("aggressive").getAsBoolean();
			boolean retreats = reader.get("retreats").getAsBoolean();
			boolean poisonous = reader.get("poisonous").getAsBoolean();
			
			int combatLevel = reader.get("combatLevel").getAsInt();
			int respawnTime = reader.get("respawn").getAsInt();
			int maxHit = reader.get("maxHit").getAsInt();
			int hitpoints = reader.get("hitpoints").getAsInt();
			
			int attackDelay = reader.get("attackDelay").getAsInt();
			
			int attackAnim = reader.get("attackAnim").getAsInt();
			int defenceAnim = reader.get("defenceAnim").getAsInt();
			int deathAnim = reader.get("deathAnim").getAsInt();
			
			int attackLevel = reader.get("attackLevel").getAsInt();
			int magicLevel = reader.get("magicLevel").getAsInt();
			int rangedLevel = reader.get("rangedLevel").getAsInt();
			int defenceLevel = reader.get("defenceLevel").getAsInt();
			
			int attackStab = reader.get("atkStab").getAsInt();
			int attackSlash = reader.get("atkSlash").getAsInt();
			int attackCrush = reader.get("atkCrush").getAsInt();
			int attackMagic = reader.get("atkMagic").getAsInt();
			int attackRanged = reader.get("atkRanged").getAsInt();
			
			int defenceStab = reader.get("defStab").getAsInt();
			int defenceSlash = reader.get("defSlash").getAsInt();
			int defenceCrush = reader.get("defCrush").getAsInt();
			int defenceMagic = reader.get("defMagic").getAsInt();
			int defenceRanged = reader.get("defRanged").getAsInt();
			
			String weakness = reader.get("weakness").getAsString();
			String slayerKey = reader.get("slayerKey").getAsString();
			int slayerRequirement = reader.get("slayerRequirement").getAsInt();
			if(!Slayer.SLAYER_LEVELS.containsKey(slayerKey) && slayerKey != null && slayerKey.length() > 1) {
				Slayer.SLAYER_LEVELS.put(slayerKey, slayerRequirement);
			}
			if(aggressive) {
				MobAggression.AGGRESSIVE.add(index);
			}
			combat = new MobDefinitionCombat(aggressive, retreats, poisonous, respawnTime, maxHit, hitpoints, attackDelay, attackAnim, defenceAnim, deathAnim, combatLevel, attackLevel, magicLevel, rangedLevel, defenceLevel, slayerRequirement, slayerKey, weakness, attackStab, attackSlash, attackCrush, attackMagic, attackRanged, defenceStab, defenceSlash, defenceCrush, defenceMagic, defenceRanged);
		}
		MobDefinition.DEFINITIONS[index] = new MobDefinition(index, name, description, size, attackable, combat);
	}
}