package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.world.PoisonType;
import com.rageps.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import com.rageps.util.json.JsonLoader;

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
		super("./data/def/combat/weapon_poison.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		PoisonType type = Objects.requireNonNull(PoisonType.valueOf(reader.get("type").getAsString()));
		CombatPoisonEffect.TYPES.put(id, type);
	}
}
