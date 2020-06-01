package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.world.locale.area.AreaManager;
import com.rageps.world.locale.loc.CircleLocation;
import com.rageps.world.locale.loc.Location;
import com.rageps.world.locale.loc.SquareLocation;
import com.rageps.util.json.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all multi area instances.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AreaMultiLoader extends JsonLoader {
	
	public AreaMultiLoader() {
		super("./data/def/areas_multi.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		boolean square = reader.has("square");
		Location loc = builder.fromJson(square ? reader.get("square") : reader.get("circle"), square ? SquareLocation.class : CircleLocation.class);
		AreaManager.get().getMultiZones().add(loc);
	}
}
