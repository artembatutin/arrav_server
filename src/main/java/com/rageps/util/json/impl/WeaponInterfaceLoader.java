package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.util.json.JsonLoader;
import com.rageps.world.entity.actor.combat.weapon.WeaponInterface;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all weapon interfaces.
 * @author lare96 <http://github.com/lare96>
 */
public final class WeaponInterfaceLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link WeaponInterfaceLoader}.
	 */
	public WeaponInterfaceLoader() {
		super("./data/def/combat/weapon_interfaces.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int[] ids = Objects.requireNonNull(builder.fromJson(reader.get("ids").getAsJsonArray(), int[].class));
		WeaponInterface interfaces = Objects.requireNonNull(builder.fromJson(reader.get("interface"), WeaponInterface.class));
		for(int i : ids) {
			if(WeaponInterface.INTERFACES.containsKey(i)) {
				throw new IllegalStateException("Value " + i + " has been defined twice in the weapon interface definitions.");
			}
			WeaponInterface.INTERFACES.put(i, interfaces);
		}
	}
}
