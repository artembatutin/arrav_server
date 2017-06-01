package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropCache;
import net.edge.world.node.entity.npc.drop.NpcDropManager;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all cached {@link NpcDrop}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDropCacheLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link NpcDropCacheLoader}.
	 */
	public NpcDropCacheLoader() {
		super("./data/json/npcs/npc_drops_cache.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		NpcDropCache table = Objects.requireNonNull(builder.fromJson(reader.get("table"), NpcDropCache.class));
		NpcDrop[] items = Objects.requireNonNull(builder.fromJson(reader.get("items"), NpcDrop[].class));
		NpcDropManager.COMMON.put(table, items);
	}
}