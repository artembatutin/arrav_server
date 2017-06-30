package net.edge.world.node.entity.npc.drop;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.util.json.JsonSaver;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.node.region.Region;

import java.util.*;

/**
 * The static-utility class that manages all of the {@link NpcDropTable}s
 * including the process of dropping the items when an {@link Npc} is killed.
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropManager {
	
	static final NpcDropTable DEFAULT = new NpcDropTable(new NpcDrop[]{}, new ItemCache[]{ItemCache.CASKETS, ItemCache.HERB_SEEDS, ItemCache.FLOWER_SEEDS, ItemCache.ALLOTMENT_SEEDS, ItemCache.CHARMS, ItemCache.LOW_RUNES, ItemCache.LOW_GEMS});
	
	/**
	 * The {@link EnumMap} consisting of the cached common {@link NpcDrop}s used
	 * across many {@link NpcDropTable}s.
	 */
	public static final EnumMap<ItemCache, NpcDrop[]> COMMON = new EnumMap<>(ItemCache.class);
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Npc}s.
	 */
	public final static Int2ObjectOpenHashMap<NpcDropTable> TABLES = new Int2ObjectOpenHashMap<>();
	
	/**
	 * Npc sharing the same table drop redirects.
	 */
	public final static Int2IntArrayMap REDIRECTS = new Int2IntArrayMap();
	
	/**
	 * Drops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * @param killer the killer, may or may not exist.
	 * @param victim the victim that was killed.
	 */
	public static void dropItems(Player killer, Npc victim) {
		NpcDropTable table = TABLES.getOrDefault(victim.getId(), DEFAULT);
		List<Item> dropItems = table.toItems(killer, victim);
		Region region = victim.getRegion();
		if(region == null)
			return;
		for(Item drop : dropItems) {
			if(drop == null)
				continue;
			region.register(new ItemNode(drop, victim.getPosition(), killer));
		}
	}
	
	public static Int2ObjectOpenHashMap<NpcDropTable> getTables() {
		return TABLES;
	}
	
	/**
	 * Serializes the drops.
	 */
	public static void serializeDrops() {
		JsonSaver drops_saver = new JsonSaver();
		for(int id : TABLES.keySet()) {
			if(REDIRECTS.containsKey(id))
				continue;
			NpcDropTable table = TABLES.get(id);
			if(table == null)
				continue;
			ObjectList<Integer> redirects = new ObjectArrayList<>();
			redirects.add(id);
			REDIRECTS.forEach((i, r) -> {
				if(r == id) {
					redirects.add(i);
				}
			});
			drops_saver.current().add("ids", new Gson().toJsonTree(redirects.toArray()));
			drops_saver.current().add("unique", new Gson().toJsonTree(table.getUnique().toArray()));
			drops_saver.current().add("common", new Gson().toJsonTree(table.getCommon().toArray()));
			drops_saver.split();
		}
		drops_saver.publish("./data/json/npcs/npc_drops2.json");
	}
	
}