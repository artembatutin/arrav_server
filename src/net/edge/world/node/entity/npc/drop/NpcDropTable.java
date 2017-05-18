package net.edge.world.node.entity.npc.drop;

import net.edge.utils.log.Log;
import net.edge.utils.log.impl.DropLog;
import net.edge.utils.rand.Chance;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.GameConstants;
import net.edge.world.World;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A container that holds the unique and common drop tables.
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropTable {
	
	/**
	 * The unique drop table that consists of both dynamic and rare drops.
	 */
	private NpcDrop[] unique;
	
	/**
	 * The common drop table that is shared with other tables.
	 */
	private final NpcDropCache[] common;
	
	/**
	 * Creates a new {@link NpcDropTable}.
	 * @param unique the unique drop table.
	 * @param common the common drop table.
	 */
	public NpcDropTable(NpcDrop[] unique, NpcDropCache[] common) {
		this.unique = unique;
		this.common = common;
	}
	
	/**
	 * Performs the necessary calculations on all of the tables in this
	 * container to determine an array of items to drop. Please note that this
	 * is not a static implementation meaning that calling this multiple times
	 * will return a different array of items.
	 * @param player the player that these calculations are being performed for.
	 * @param npc    the npc that was killed.
	 * @return the array of items that were calculated.
	 */
	public List<Item> toItems(Player player, Npc victim) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		// Instantiate the random generator, the list of items to drop, the list
		// for the rare items, the common table, and a list that contains a
		// shuffled copy of the unique table.
		List<Item> items = new LinkedList<>();
		NpcDropCache cache = RandomUtils.random(common.length > 0 ? common : NpcDropManager.DEFAULT.common);
		
		// Determines if the rare, common, and dynamic tables should be rolled.
		// The breakdown of each of the formulas are touched upon later on.
		boolean rollRare = random.nextInt(5) == 0; // 20% chance.
		int rate = (player.getEquipment().getId(Equipment.RING_SLOT) == 2572 ? 4 : 8);
		boolean rollCommon = random.nextInt(rate) == 0; // 12.5%-25% chance.
		boolean rollDynamic = random.nextBoolean(); // 50% chance.
		
		// Iterate through the unique table, drop ALWAYS items, roll a RARE+
		// item if possible, and roll dynamic items if possible.
		int amount = 0;
		
		for(NpcDrop drop : unique) {
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
		
		if(rollCommon) {
			// n (n = 12.5% chance, 25% if wearing Ring of Wealth)
			// Chance to roll an item from the common table, pick one drop
			// from the table and roll it.
			NpcDrop next = RandomUtils.random(NpcDropManager.COMMON.get(cache));
			if(next.roll(random))
				items.add(next.toItem());
		}
		
		return items;
	}
	
	public void setUnique(NpcDrop[] unique) {
		this.unique = unique;
	}
	
	public NpcDrop[] getUnique() {
		return unique;
	}
	
	public NpcDropCache[] getCommon() {
		return common;
	}
	
}