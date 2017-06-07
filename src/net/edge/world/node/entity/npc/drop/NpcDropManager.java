package net.edge.world.node.entity.npc.drop;

import com.google.gson.Gson;
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
	
	public static final NpcDropTable DEFAULT = new NpcDropTable(new NpcDrop[]{}, new NpcDropCache[]{NpcDropCache.CASKETS, NpcDropCache.HERB_SEEDS, NpcDropCache.FLOWER_SEEDS, NpcDropCache.ALLOTMENT_SEEDS, NpcDropCache.CHARMS, NpcDropCache.LOW_RUNES, NpcDropCache.LOW_GEMS, NpcDropCache.LOW_EQUIPMENT, NpcDropCache.LOW_RESOURCES});
	
	/**
	 * The {@link EnumMap} consisting of the cached common {@link NpcDrop}s used
	 * across many {@link NpcDropTable}s.
	 */
	public static final EnumMap<NpcDropCache, NpcDrop[]> COMMON = new EnumMap<>(NpcDropCache.class);
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Npc}s.
	 */
	public final static Map<Integer, NpcDropTable> TABLES = new HashMap<>();
	
	/**
	 * Npc redirects.
	 */
	public final static Map<Integer, Integer> REDIRECTS = new HashMap<>();
	
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
	
	public static Map<Integer, NpcDropTable> getTables() {
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
			List<Integer> redirects = new ArrayList<>();
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