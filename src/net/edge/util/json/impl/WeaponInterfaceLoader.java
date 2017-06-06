package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.combat.weapon.WeaponInterface;

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
		super("./data/json/equipment/weapon_interfaces.json");
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
