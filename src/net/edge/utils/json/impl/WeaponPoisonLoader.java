package net.edge.utils.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.utils.json.JsonLoader;
import net.edge.world.content.combat.effect.CombatPoisonEffect;
import net.edge.world.node.entity.model.PoisonType;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all weapons that poison
 * players.
 * @author lare96 <http://github.com/lare96>
 */
public final class WeaponPoisonLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link WeaponPoisonLoader}.
	 */
	public WeaponPoisonLoader() {
		super("./data/json/equipment/weapon_poison.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		PoisonType type = Objects.requireNonNull(PoisonType.valueOf(reader.get("type").getAsString()));
		CombatPoisonEffect.TYPES.put(id, type);
	}
}
