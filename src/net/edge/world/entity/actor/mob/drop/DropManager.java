package net.edge.world.entity.actor.mob.drop;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.util.json.JsonSaver;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.region.Region;

import java.util.*;

/**
 * The static-utility class that manages all of the {@link DropTable}s
 * including the process of dropping the items when an {@link Mob} is killed.
 * @author lare96 <http://github.org/lare96>
 */
public final class DropManager {

	static final DropTable DEFAULT = new DropTable(new Drop[]{}, new ItemCache[]{ItemCache.HERB_SEEDS, ItemCache.FLOWER_SEEDS, ItemCache.ALLOTMENT_SEEDS, ItemCache.CHARMS, ItemCache.LOW_RUNES, ItemCache.LOW_GEMS});

	/**
	 * The {@link EnumMap} consisting of the cached common {@link Drop}s used
	 * across many {@link DropTable}s.
	 */
	public static final EnumMap<ItemCache, Drop[]> COMMON = new EnumMap<>(ItemCache.class);

	/**
	 * The {@link HashMap} that consists of the drops for {@link Mob}s.
	 */
	public final static Int2ObjectOpenHashMap<DropTable> TABLES = new Int2ObjectOpenHashMap<>();

	/**
	 * Mob sharing the same table drop redirects.
	 */
	public final static Int2IntArrayMap REDIRECTS = new Int2IntArrayMap();

	/**
	 * Drops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * @param killer the killer, may or may not exist.
	 * @param victim the victim that was killed.
	 */
	public static void dropItems(Player killer, Mob victim) {
		DropTable table = TABLES.getOrDefault(victim.getId(), DEFAULT);
		List<Item> dropItems = table.toItems(killer, victim);

		if(victim.getMaxHealth() >= 500 && RandomUtils.inclusive(50) == 1) {
			dropItems.add(new Item(450));
		}

		Region region = victim.getRegion();
		if(region == null)
			return;
		for(Item drop : dropItems) {
			if(drop == null)
				continue;
			region.register(new GroundItem(drop, victim.getPosition(), killer));
		}
	}

	public static Int2ObjectOpenHashMap<DropTable> getTables() {
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
			DropTable table = TABLES.get(id);
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
		drops_saver.publish("./data/def/mob/mob_drops2.json");
	}

}