package com.rageps.world.entity.actor.mob.drop;

import com.rageps.GameConstants;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * A container that holds the unique and common drop tables.
 * @author Artem Batutin
 * @author Tamatea <tamateea@gmail.com>
 */
public final class DropTable {
	
	/**
	 * The drop list that consists of common drops.
	 */
	private final ObjectList<Drop> drops;

	/**
	 * The drop list of drops which will always be dropped.
	 */
	private final ObjectList<Drop> alwaysDrops;

	/**
	 * The id of the npc associated with this drop table.
	 */
	private final int id;

	/**
	 * Creates a new {@link DropTable}.
	 */
	public DropTable(int id, ObjectArrayList<Drop> drops) {
		this.id = id;
		this.drops = new ObjectArrayList<>(drops);
		this.alwaysDrops = this.drops.stream().filter(Drop::isAlways).collect(Collectors.toCollection(ObjectArrayList::new));
	}

	/**
	 * Gets the id of the npc associated with this drop table.
	 * @return the id.
	 */
	public int getNpcId() {
		return id;
	}

	/**
	 * Performs the necessary calculations on all of the tables in this
	 * container to determine an array of items to drop. Please note that this
	 * is not a static implementation meaning that calling this multiple times
	 * will return a different array of items.
	 * @param player The player that these calculations are being performed for.
	 * @param simulation If the roll is being simulated, or is a true drop from a killed npc.
	 * @return The array of items that were calculated.
	 */
	ObjectList<Item> toItems(Player player, boolean simulation) {
		ObjectList<Item> items = new ObjectArrayList<>();
		int amount = 0;
		if(!alwaysDrops.isEmpty()) {
			for(Drop drop : alwaysDrops) {
				if(drop == null)
					continue;
				items.add(drop.toItem());
			}
		}
		if(!drops.isEmpty()) {
			for(Drop drop : drops) {//Will iterate through the drop list testing the most rare items first.
				if(drop == null)	//Some people shuffle this list and then test chances agains GameConstants.DROP_THRESHOLD
					continue;		//However doing that makes the drop rates not reliable as it's chance may not even be tested.
				if(drop.isAlways())//as always drops are tested up there.
					continue;
					if(amount++ <= GameConstants.DROP_THRESHOLD && (simulation ? drop.getDropChance().simulate(player) : drop.getDropChance().test(player))) {
						items.add(drop.toItem());
				}
			}
		}
		return items;
	}


	/**
	 * Simulated x amount of rolls of this {@link DropTable}, then merges and maps all of the results
	 * together.
	 * @param player The player simulating the roll.
	 * @param amt The amount of times to simulate the drop table.
	 * @return The merged drop table results.
	 */
	public Object2ObjectMap<Integer, Integer> simulateRoll(Player player, int amt) {
		Object2ObjectMap<Integer, Integer> drops = new Object2ObjectArrayMap<>();
		for(int roll = 0; roll < amt; roll++) {
			ObjectList<Item> rolled = toItems(player, true);
			for(Item item : rolled) {
				if(item == null)
					continue;
				drops.merge(item.getId(), item.getAmount(), Integer::sum);
			}
		}
		return drops;
	}
	
	/**
	 * Sorting the drop table by chance tiers.
	 */
	public void sort() {
		drops.sort(Comparator.comparingInt(Drop::getChance));
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
				if(d.equals(drop))
					return true;
				ItemDefinition def = ItemDefinition.get(d.getId());
				if(def.getName().equalsIgnoreCase(defDrop.getName())) {
					return true;
				}
			}
		return false;
	}

	/**
	 * The drops contained by this drop table.
	 * @return The drops.
	 */
	public ObjectList<Drop> getDrops() {
		return drops;
	}

}