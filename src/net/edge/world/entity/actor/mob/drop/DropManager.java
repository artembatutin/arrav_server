package net.edge.world.entity.actor.mob.drop;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Bone;
import net.edge.util.json.JsonSaver;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.locale.Position;

import java.util.*;

/**
 * The static-utility class that manages all of the {@link DropTable}s
 * including the process of dropping the items when an {@link Mob} is killed.
 * @author lare96 <http://github.org/lare96>
 */
public final class DropManager {
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Mob}s.
	 */
	private final static Int2ObjectOpenHashMap<DropTable> TABLES = new Int2ObjectOpenHashMap<>();

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
		DropTable table = TABLES.get(victim.getId());
		if(table == null) {
			return;
		}
		Position pos = victim.getPosition();
		if(victim.getId() == 3847) {//sea troll
			pos = new Position(2346, 3700);
		}
		final Position p = pos;
		victim.getRegion().ifPresent(r -> {
			List<Item> dropItems = table.toItems(killer, victim);
			for(Item drop : dropItems) {
				if(drop == null)
					continue;

				boolean bonecrusher = killer.getInventory().contains(18337);
				if(bonecrusher) {
					Optional<Bone> bone = Bone.getBone(drop.getId());
					bone.ifPresent(b -> Skills.experience(killer, b.getExperience() / 2, Skills.PRAYER));
					
					continue;//don't submit bone to floor but dont break the loop either.
				}
				r.register(new GroundItem(drop, p, killer));
			}
		});
	}

	public static Int2ObjectOpenHashMap<DropTable> getTables() {
		return TABLES;
	}
	
	public static Int2IntArrayMap getRedirects() {
		return REDIRECTS;
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
			table.sort();
			ObjectList<Integer> redirects = new ObjectArrayList<>();
			redirects.add(id);
			REDIRECTS.forEach((i, r) -> {
				if(r == id) {
					redirects.add(i);
				}
			});
			drops_saver.current().add("ids", new Gson().toJsonTree(redirects.toArray()));
			drops_saver.current().add("drop", new Gson().toJsonTree(table.getDrops().toArray()));
			drops_saver.split();
		}
		drops_saver.publish("./data/def/mob/mob_drops2.json");
	}

}