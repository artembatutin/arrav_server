package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.combat.ranged.CombatRangedAmmunition;
import net.edge.content.combat.ranged.CombatRangedDetails;
import net.edge.content.combat.ranged.CombatRangedDetails.CombatRangedWeapon;
import net.edge.content.combat.ranged.CombatRangedType;
import net.edge.world.node.item.ItemDefinition;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all combat ranged bows.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CombatRangedBowLoader extends JsonLoader {
	
	/**
	 * Constructs a new {@link CombatRangedBowLoader}.
	 */
	public CombatRangedBowLoader() {
		super("./data/json/equipment/combat_ranged_bows.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int[] ids = reader.get("item").isJsonArray() ? builder.fromJson(reader.get("item"), int[].class) : new int[]{reader.get("item").getAsInt()};
		CombatRangedType type = Objects.requireNonNull(builder.fromJson(reader.get("type"), CombatRangedType.class));
		int delay = reader.get("delay").getAsInt();
		CombatRangedAmmunition[] ammunitions = Objects.requireNonNull(builder.fromJson(reader.get("ammunitions"), CombatRangedAmmunition[].class));
		if(type == null) {
			throw new IllegalStateException("Invalid bow type for [id = " + ids[0] + ", name = " + ItemDefinition.DEFINITIONS[ids[0]].getName() + "]");
		}
		
		if((type.equals(CombatRangedType.SHORTBOW) || type.equals(CombatRangedType.LONGBOW) || type.equals(CombatRangedType.CROSSBOW)) && Arrays.stream(ammunitions).anyMatch(d -> d == null)) {
			throw new IllegalStateException("Invalid ammunition for [id = " + ids[0] + ", name = " + ItemDefinition.DEFINITIONS[ids[0]].getName() + "]");
		}
		
		Arrays.stream(ids).forEach(id -> CombatRangedDetails.RANGED_WEAPONS.put(id, new CombatRangedWeapon(ammunitions, type, delay)));
	}
	
}
