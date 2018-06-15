package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.arrav.content.skill.slayer.Slayer;
import net.arrav.content.skill.slayer.SlayerLocationPolicy;
import net.arrav.util.json.JsonLoader;
import net.arrav.world.locale.Position;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all slayer locations.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class SlayerLocationLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link SlayerLocationLoader}.
	 */
	public SlayerLocationLoader() {
		super("./data/def/slayer/slayer_locations.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String key = Objects.requireNonNull(reader.get("key").getAsString());
		int price = reader.get("price").getAsInt();
		Position[] positions = reader.get("position").isJsonArray() ? Objects.requireNonNull(builder.fromJson(reader.get("position").getAsJsonArray(), Position[].class)) : new Position[]{Objects.requireNonNull(builder.fromJson(reader.get("position").getAsJsonObject(), Position.class))};
		Slayer.SLAYER_LOCATIONS.put(key, new SlayerLocationPolicy(positions, price));
	}
}
