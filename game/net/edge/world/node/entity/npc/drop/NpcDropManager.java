package net.edge.world.node.entity.npc.drop;

import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcDefinition;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.region.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The static-utility class that manages all of the {@link NpcDropTable}s
 * including the process of dropping the items when an {@link Npc} is killed.
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropManager {
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Npc}s.
	 */
	private static final Map<Integer, NpcDropTable> TABLES = new HashMap<>();
	
	/**
	 * Drops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * @param killer the killer, may or may not exist.
	 * @param victim the victim that was killed.
	 */
	public static void dropItems(Player killer, Npc victim) {
		NpcDropTable table = TABLES.get(victim.getId());
		if(table == null) {
			killer.message(victim.getDefinition().getName() + " doesn't have a drop table, please suggest one on the forums.");
			return;
		}
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
	
	public static void dump() {
		JSONArray list = new JSONArray();
		int drops = 0;
		int tables = 0;
		int npcsShare = 0;
		boolean[] redirect = new boolean[NpcDefinition.DEFINITIONS.length];
		Map<Integer, Integer> redirects = new HashMap<>();
		for(int id : TABLES.keySet()) {
			if(id < 0)
				continue;
			if(redirect[id])
				continue;
			NpcDropTable orig = TABLES.get(id);
			for(int id2 : TABLES.keySet()) {
				if(id2 < 0)
					continue;
				if(id == id2)
					continue;
				NpcDropTable check = TABLES.get(id2);
				boolean same = true;
				if(orig.getDrops().size() == check.getDrops().size()) {
					for(NpcDrop dropCheck : check.getDrops()) {
						if(dropCheck == null)
							continue;
						if(!orig.contains(dropCheck)) {
							same = false;
							break;
						}
					}
				} else
					same = false;
				if(same) {
					if(redirect[id]) {
						redirects.put(id2, redirects.get(id));
					} else {
						redirects.put(id2, id);
					}
					redirect[id2] = true;
				}
			}
		}
		for(int id : TABLES.keySet()) {
			if(id < 0)
				continue;
			if(redirect[id])
				continue;
			JSONObject obj = new JSONObject();
			JSONArray ids = new JSONArray();
			ids.add(id);
			for(int k : redirects.keySet()) {
				int v = redirects.get(k);
				if(v == id) {
					npcsShare += 1;
					ids.add(k);
				}
			}
			obj.put("ids", ids);
			
			JSONArray unique = new JSONArray();
			TABLES.get(id).sort();
			for(NpcDrop drop : TABLES.get(id).getDrops()) {
				if(drop == null)
					continue;
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
			list.add(obj);
			tables += 1;
		}
		
		
		JSONObject res = new JSONObject();
		res.put("res", list);
		try (FileWriter file = new FileWriter("./data/json/npcsdrops.json")) {
			
			file.write(res.toJSONString());
			file.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Drops: " + drops + " in " + tables + " tables with " + npcsShare + " shared tables between npcs.");
		
	}
	
}