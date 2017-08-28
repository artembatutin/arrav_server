package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.world.entity.actor.player.assets.ShieldAnimation;
import net.edge.world.entity.item.ItemDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all shield animations.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ShieldAnimationLoader extends JsonLoader {
	
	/**
	 * Constructs a new {@link ShieldAnimationLoader}.
	 */
	public ShieldAnimationLoader() {
		super("./data/def/combat/shield_animations.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String[] names = reader.get("names").isJsonArray() ? Objects.requireNonNull(builder.fromJson(reader.get("names").getAsJsonArray(), String[].class)) : new String[]{reader.get("names").getAsString()};
		int block = reader.get("block").getAsInt();
		ShieldAnimation animation = new ShieldAnimation(block);
		
		Arrays.stream(names).forEach(i -> {
			List<ItemDefinition> def = ItemDefinition.collect(i);
			def.forEach(d -> ShieldAnimation.ANIMATIONS.put(d.getId(), animation));
		});
	}
	
}
