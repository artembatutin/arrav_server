package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rageps.world.locale.area.Area;
import com.rageps.world.locale.area.AreaManager;
import com.rageps.world.locale.loc.CircleLocation;
import com.rageps.world.locale.loc.Location;
import com.rageps.world.locale.loc.SquareLocation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.util.json.JsonLoader;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all area instances.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AreaLoader extends JsonLoader {
	
	public AreaLoader() {
		super("./data/def/areas.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		JsonArray location = reader.get("location").getAsJsonArray();
		ObjectList<Area.AreaLocation> locations = new ObjectArrayList<>();
		
		boolean multi;
		boolean teleport;
		boolean summon;
		
		for(JsonElement element : location) {
			multi = element.getAsJsonObject().has("multi") && element.getAsJsonObject().get("multi").getAsBoolean();
			teleport = !element.getAsJsonObject().has("teleport") || element.getAsJsonObject().get("teleport").getAsBoolean();
			summon = !element.getAsJsonObject().has("summon") || element.getAsJsonObject().get("summon").getAsBoolean();
			boolean square = element.getAsJsonObject().has("square");
			Location loc = builder.fromJson(square ? element.getAsJsonObject().get("square") : element.getAsJsonObject().get("circle"), square ? SquareLocation.class : CircleLocation.class);
			locations.add(new Area.AreaLocation(loc, teleport, summon));
			if(multi) {
				AreaManager.get().getMultiZones().add(loc);
			}
		}
		AreaManager.get().getAreas().put(name, new Area(name, locations));
	}
}
