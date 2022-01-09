package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.util.json.JsonLoader;
import com.rageps.world.entity.actor.combat.weapon.WeaponAnimation;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all weapon animations.
 * @author lare96 <http://github.com/lare96>
 */
public final class WeaponAnimationLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link WeaponAnimationLoader}.
	 */
	public WeaponAnimationLoader() {
		super("./data/def/combat/weapon_animations.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int[] id = reader.get("id").isJsonArray() ? Objects.requireNonNull(builder.fromJson(reader.get("id").getAsJsonArray(), int[].class)) : new int[]{reader.get("id").getAsInt()};
		int stand = reader.get("stand").getAsInt();
		int walk = reader.get("walk").getAsInt();
		int run = reader.get("run").getAsInt();
		int block = reader.get("block").getAsInt();
		int[] attack = Objects.requireNonNull(builder.fromJson(reader.get("attack").getAsJsonArray(), int[].class));
		WeaponAnimation animation = new WeaponAnimation(stand, walk, run, block, attack);
		
		if(id.length == 1)
			WeaponAnimation.ANIMATIONS.put(id[0], animation);
		else
			Arrays.stream(id).forEach(i -> WeaponAnimation.ANIMATIONS.put(i, animation));
	}
}
