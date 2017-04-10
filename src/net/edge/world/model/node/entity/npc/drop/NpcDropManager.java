package net.edge.world.model.node.entity.npc.drop;

import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemNode;
import net.edge.world.model.node.region.Region;

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
	public static Map<Integer, NpcDropTable> TABLES = new HashMap<>();
	
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
	
	/*public static void dump() {
		JSONArray list = new JSONArray();
		int npcs = 0;
		int drops = 0;
		for(int id : TABLES.keySet()) {
			if(id < 0)
				continue;
			JSONObject obj = new JSONObject();
			
			JSONArray ids = new JSONArray();
			int pos = 0;
			ids.add(id);
			obj.put("ids", ids);
			
			JSONArray unique = new JSONArray();
			for(NpcDrop drop : TABLES.get(id).getUnique()) {
				JSONObject dropa = new JSONObject();
				if(drop == null)
					continue;
				if(drop.getId() < 0)
					continue;
				dropa.put("id", drop.getId());
				dropa.put("minimum", drop.getMinimum());
				dropa.put("maximum", drop.getMaximum());
				dropa.put("chance", drop.getChance().toString());
				unique.add(dropa);
				drops += 1;
			}
			obj.put("drops", unique);
			npcs += pos;
			list.add(obj);
		}
		
		System.out.println("Total drops: " + drops + " for " + npcs);
		
		JSONObject res = new JSONObject();
		res.put("res", list);
		try (FileWriter file = new FileWriter("./drops.json")) {
			
			file.write(res.toJSONString());
			file.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
}