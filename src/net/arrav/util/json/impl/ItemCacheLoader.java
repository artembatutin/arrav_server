package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.arrav.util.json.JsonLoader;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.item.ItemCache;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all cached {@link Drop}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemCacheLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link ItemCacheLoader}.
	 */
	public ItemCacheLoader() {
		super("./data/def/item/item_cache.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		ItemCache table = Objects.requireNonNull(builder.fromJson(reader.get("table"), ItemCache.class));
		Drop[] items = Objects.requireNonNull(builder.fromJson(reader.get("items"), Drop[].class));
		ItemCache.COMMON.put(table, items);
	}
}