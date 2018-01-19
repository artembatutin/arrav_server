package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.arrav.util.json.JsonLoader;
import net.arrav.world.entity.actor.combat.ranged.RangedAmmunition;
import net.arrav.world.entity.actor.combat.ranged.RangedWeaponDefinition;
import net.arrav.world.entity.actor.combat.ranged.RangedWeaponType;
import net.arrav.world.entity.item.ItemDefinition;

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
		super("./data/def/combat/combat_ranged_bows.json");
	}
	
	public static Int2ObjectArrayMap<RangedWeaponDefinition> DEFINITIONS;

	@Override
	protected void initialize(int size) {
		DEFINITIONS = new Int2ObjectArrayMap<>(size);
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		try {
			int[] ids = reader.get("item").isJsonArray() ? builder.fromJson(reader.get("item"), int[].class) : new int[]{reader.get("item").getAsInt()};
			RangedWeaponType type = Objects.requireNonNull(RangedWeaponType.valueOf(reader.get("type").getAsString()));
			RangedAmmunition[] ammunitions = Objects.requireNonNull(builder.fromJson(reader.get("ammunitions"), RangedAmmunition[].class));
			
			if(type == null || ammunitions == null) {
				throw new IllegalStateException("Invalid ranged definition for [id = " + ids[0] + ", name = " + ItemDefinition.DEFINITIONS[ids[0]].getName() + "]");
			}
			
			RangedWeaponDefinition def = new RangedWeaponDefinition(type, ammunitions);
			Arrays.stream(ids).forEach(i -> DEFINITIONS.put(i, def));
		} catch(Exception ignored) {
		}
	}
	
}
