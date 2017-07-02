package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.locale.loc.Location;
import net.edge.util.json.JsonLoader;
import net.edge.locale.loc.CircleLocation;
import net.edge.locale.loc.SquareLocation;
import net.edge.locale.area.Area;
import net.edge.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all area instances.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AreaLoader extends JsonLoader {
	
	public AreaLoader() {
		super("./data/json/areas.json");
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
				World.getAreaManager().getMultiZones().add(loc);
			}
		}
		World.getAreaManager().getAreas().put(name, new Area(name, locations));
	}
}
