package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.util.json.JsonLoader;
import com.rageps.world.entity.actor.mob.drop.Drop;
import com.rageps.world.entity.item.cached.CachedItem;
import com.rageps.world.entity.item.cached.ItemCache;

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
		CachedItem[] items = Objects.requireNonNull(builder.fromJson(reader.get("items"), CachedItem[].class));
		ItemCache.COMMON.put(table, items);
	}
}