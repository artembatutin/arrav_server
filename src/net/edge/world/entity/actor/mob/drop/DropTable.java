package net.edge.world.entity.actor.mob.drop;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.world.entity.item.ItemDefinition;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.GameConstants;
import net.edge.util.log.Log;
import net.edge.util.log.impl.DropLog;
import net.edge.util.rand.Chance;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A container that holds the unique and common drop tables.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class DropTable {
	
	/**
	 * The drop list that consists of both dynamic and rare drops.
	 */
	private final ObjectList<Drop> drops;
	
	/**
	 * Creates a new {@link DropTable}.
	 * @param drops the drops array.
	 */
	public DropTable(Drop... drops) {
		this.drops = new ObjectArrayList<>(drops);
	}
	
	/**
	 * Creates a new {@link DropTable}.
	 * @param drops the drops list.
	 */
	public DropTable(ObjectList<Drop> drops) {
		this.drops = drops;
	}
	
	/**
	 * Performs the necessary calculations on all of the tables in this
	 * container to determine an array of items to drop. Please note that this
	 * is not a static implementation meaning that calling this multiple times
	 * will return a different array of items.
	 * @param player the player that these calculations are being performed for.
	 * @param victim    the npc that was killed.
	 * @return the array of items that were calculated.
	 */
	List<Item> toItems(Player player, Mob victim) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		ObjectList<Item> items = new ObjectArrayList<>();
		
		// Determines if the rare, common, and dynamic tables should be rolled.
		// The breakdown of each of the formulas are touched upon later on.
		int rate = (player.getEquipment().getId(Equipment.RING_SLOT) == 2572 ? 4 : 5);
		boolean rollRare = random.nextInt(rate) == 0; // 20%/25% chance.
		boolean rollDynamic = random.nextBoolean(); // 50% chance.
		
		// Iterate through the unique table, drop ALWAYS items, roll a RARE+
		// item if possible, and roll dynamic items if possible.
		int amount = 0;
		
		for(Drop drop : drops) {
			if(drop.getChance() == Chance.ALWAYS) {
				// 100% Chance to drop an item from the always table, the lowest chance tier.
				items.add(drop.toItem());
			} else if(rollRare && drop.isRare()) {
				// 20% Chance to roll an item from the rare table, pick one drop
				// from the table and roll it.
				if(drop.roll(random)) {
					Item item = drop.toItem();
					items.add(item);
					World.getLoggingManager().write(Log.create(new DropLog(player, victim.getDefinition(), item)));
					if(drop.getChance() == Chance.EXTREMELY_RARE) {
						World.get().message(player.getFormatUsername() + " just got an extremely rare drop: " + item.getDefinition().getName());
					}
				}
				rollRare = false;
			} else if(rollDynamic && !drop.isRare()) {
				// 50% Chance to roll an item from the dynamic table, pick one
				// drop from the table and roll it.
				if(amount++ == GameConstants.DROP_THRESHOLD)
					rollDynamic = false;
				if(drop.roll(random))
					items.add(drop.toItem());
			}
		}
		
		return items;
	}
	
	/**
	 * Sorting the drop table by chance tiers.
	 */
	public void sort() {
		drops.sort(Comparator.comparingInt(o -> o.getChance().ordinal()));
	}
	
	/**
	 * Looks to find if this drop has a specific drop.
	 * @param drop the drop seeking for.
	 * @return if found, true otherwise false.
	 */
	public boolean contains(Drop drop) {
		ItemDefinition defDrop = ItemDefinition.get(drop.getId());
		for(Drop d : drops) {
			if(d == null)
				continue;
			if(d.getId() == drop.getId() && d.getChance() == drop.getChance() && d.getMinimum() == drop.getMinimum())
				return true;
			ItemDefinition def = ItemDefinition.get(d.getId());
			if(def.getName().equalsIgnoreCase(defDrop.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public ObjectList<Drop> getDrops() {
		return drops;
	}
	
}