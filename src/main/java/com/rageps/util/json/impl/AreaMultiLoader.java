package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.util.json.JsonLoader;
import com.rageps.world.locale.loc.Area;
import com.rageps.world.locale.loc.CircleArea;
import com.rageps.world.locale.loc.Locations;
import com.rageps.world.locale.loc.SquareArea;

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
		Area loc = builder.fromJson(square ? reader.get("square") : reader.get("circle"), square ? SquareArea.class : CircleArea.class);
		Locations.MULTI_AREA.add(loc);
	}
}
