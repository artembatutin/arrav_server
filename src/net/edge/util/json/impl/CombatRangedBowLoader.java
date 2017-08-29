package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.util.json.JsonLoader;
import net.edge.world.entity.actor.combat.ranged.RangedAmmunition;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponDefinition;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponType;
import net.edge.world.entity.item.ItemDefinition;

import java.util.*;

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
	private static Set<String> missing;
	
	@Override
	protected void initialize(int size) {
		DEFINITIONS = new Int2ObjectArrayMap<>(size);
		missing = new HashSet<>();
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		try {
			int[] ids = reader.get("item").isJsonArray() ? builder.fromJson(reader.get("item"), int[].class) : new int[]{reader.get("item").getAsInt()};
			RangedWeaponType type = Objects.requireNonNull(RangedWeaponType.valueOf(reader.get("type").getAsString()));
			RangedAmmunition[] ammunitions = Objects.requireNonNull(builder.fromJson(reader.get("ammunitions"), RangedAmmunition[].class));
			
			if(type == null) {
				missing.add(reader.get("type").getAsString());
				throw new IllegalStateException("Invalid bow type for [id = " + ids[0] + ", name = " + ItemDefinition.DEFINITIONS[ids[0]].getName() + "]");
			}
			
			if((type.equals(RangedWeaponType.SHOT) || type.equals(RangedWeaponType.THROWN)) && Arrays.stream(ammunitions).anyMatch(Objects::isNull)) {
				JsonArray jarray = reader.get("ammunitions").getAsJsonArray();
				List<RangedAmmunition> newList = new ArrayList<>();
				for(JsonElement next : jarray) {
					if(Arrays.stream(RangedAmmunition.values()).noneMatch(ammo -> ammo.name().equalsIgnoreCase(next.getAsString()))) {
						missing.add(next.getAsString());
					} else {
						newList.add(RangedAmmunition.valueOf(next.getAsString()));
					}
				}
				if(newList.size() < jarray.size()) {
					ammunitions = newList.toArray(new RangedAmmunition[newList.size()]);
				}
				//                throw new IllegalStateException("Invalid ammunition for [id = " + ids[0] + ", name = " + ItemDefinition.DEFINITIONS[ids[0]].getName() + "]");
			}
			
			RangedWeaponDefinition def = new RangedWeaponDefinition(type, ammunitions);
			Arrays.stream(ids).forEach(i -> DEFINITIONS.put(i, def));
		} catch(Exception ignored) {
		}
	}
	
	@Override
	public void end() {
		missing.forEach(System.out::println);
	}
}
