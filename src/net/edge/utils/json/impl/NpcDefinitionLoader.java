package net.edge.utils.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.utils.json.JsonLoader;
import net.edge.world.content.skill.slayer.Slayer;
import net.edge.world.node.entity.npc.NpcAggression;
import net.edge.world.node.entity.npc.NpcDefinition;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all npc definitions.
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDefinitionLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link NpcDefinitionLoader}.
	 */
	public NpcDefinitionLoader() {
		super("./data/json/npcs/npc_definitions.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int index = reader.get("id").getAsInt();
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		String description = Objects.requireNonNull(reader.get("description").getAsString());
		int combatLevel = reader.get("combatLevel").getAsInt();
		int size = reader.get("size").getAsInt();
		
		boolean attackable = reader.get("attackable").getAsBoolean();
		boolean aggressive = reader.get("aggressive").getAsBoolean();
		boolean retreats = reader.get("retreats").getAsBoolean();
		boolean poisonous = reader.get("poisonous").getAsBoolean();
		
		int respawnTime = reader.get("respawn").getAsInt();
		int maxHit = reader.get("maxHit").getAsInt() * 10;
		int hitpoints = reader.get("hitpoints").getAsInt();
		
		int attackSpeed = reader.get("attackSpeed").getAsInt();
		
		int attackAnim = reader.get("attackAnim").getAsInt();
		int defenceAnim = reader.get("defenceAnim").getAsInt();
		int deathAnim = reader.get("deathAnim").getAsInt();
		
		int attackLevel = reader.get("attackLevel").getAsInt();
		int magicLevel = reader.get("magicLevel").getAsInt();
		int rangedLevel = reader.get("rangedLevel").getAsInt();
		int defenceLevel = reader.get("defenceLevel").getAsInt();
		
		String weakness = reader.get("weakness").getAsString();
		String slayerKey = reader.get("slayerKey").getAsString();
		int slayerRequirement = reader.get("slayerRequirement").getAsInt();
		if(!Slayer.SLAYER_LEVELS.containsKey(slayerKey)) {
			Slayer.SLAYER_LEVELS.put(slayerKey, slayerRequirement);
		}
		
		NpcDefinition.DEFINITIONS[index] = new NpcDefinition(index, name, description, combatLevel, size, attackable, aggressive, retreats, poisonous, respawnTime, maxHit, hitpoints, attackSpeed, attackAnim, defenceAnim, deathAnim, attackLevel, magicLevel, rangedLevel, defenceLevel, slayerRequirement, slayerKey, weakness);
		
		if(aggressive)
			NpcAggression.AGGRESSIVE.add(index);
	}
}