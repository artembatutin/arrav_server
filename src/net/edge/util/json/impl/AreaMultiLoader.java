package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.world.locale.loc.CircleLocation;
import net.edge.world.locale.loc.Location;
import net.edge.world.locale.loc.SquareLocation;
import net.edge.util.json.JsonLoader;
import net.edge.world.World;

/**
 * The {@link JsonLoader} implementation that loads all multi area instances.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AreaMultiLoader extends JsonLoader {
	
	public AreaMultiLoader() {
		super("./data/json/areas_multi.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		boolean square = reader.has("square");
		Location loc = builder.fromJson(square ? reader.get("square") : reader.get("circle"), square ? SquareLocation.class : CircleLocation.class);
		World.getAreaManager().getMultiZones().add(loc);
	}
}
