package net.edge.utils.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.edge.utils.json.JsonLoader;
import net.edge.world.model.locale.SquareLocation;
import net.edge.world.model.locale.area.Area;
import net.edge.world.World;
import net.edge.world.model.locale.CircleLocation;

import java.util.ArrayList;
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
		
		List<Area.AreaLocation> locations = new ArrayList<>();
		
		boolean multi;
		boolean teleport;
		boolean summon;
		
		Iterator<JsonElement> iterator = location.iterator();
		
		while(iterator.hasNext()) {
			JsonElement element = iterator.next();
			
			multi = !element.getAsJsonObject().has("multi") ? false : element.getAsJsonObject().get("multi").getAsBoolean();
			teleport = !element.getAsJsonObject().has("teleport") ? true : element.getAsJsonObject().get("teleport").getAsBoolean();
			summon = !element.getAsJsonObject().has("summon") ? true : element.getAsJsonObject().get("summon").getAsBoolean();
			
			boolean square = element.getAsJsonObject().has("square");
			
			locations.add(new Area.AreaLocation(builder.fromJson(square ? element.getAsJsonObject().get("square") : element.getAsJsonObject().get("circle"), square ? SquareLocation.class : CircleLocation.class), multi, teleport, summon));
		}
		
		World.getAreaManager().getAreas().put(name, new Area(name, locations));
	}
}
