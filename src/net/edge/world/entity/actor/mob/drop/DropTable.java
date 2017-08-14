package net.edge.world.entity.actor.mob.drop;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.market.MarketItem;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Bone;
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
	ObjectList<Item> toItems(Player player, Mob victim) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		ObjectList<Item> items = new ObjectArrayList<>();
		boolean dropRare = random.nextBoolean();
		boolean dropDynamic = random.nextBoolean();
		int amount = 0;
		boolean rare = true;
		for(Drop drop : drops) {
			if(drop == null)
				continue;
			if(drop.getChance() == Chance.ALWAYS) {
				//bone crusher
				if(player.getInventory().contains(18337)) {
					Optional<Bone> bone = Bone.getBone(drop.getId());
					bone.ifPresent(b -> Skills.experience(player, b.getExperience() * 1.5, Skills.PRAYER));
					if(bone.isPresent())
						continue;
				}
				items.add(drop.toItem());
			} else if(dropRare && drop.isRare() && rare) {
				boolean row = player.getEquipment().getId(Equipment.RING_SLOT) == 2572 && random.nextInt(50) == 1;
				if(drop.roll(random) || row) {
					if(row)
						player.message("Your ring of wealth got you a rare drop!");
					Item item = drop.toItem();
					items.add(item);
					int val = MarketItem.get(item.getId()) != null ? MarketItem.get(item.getId()).getPrice() * item.getAmount() : 0;
					World.getLoggingManager().write(Log.create(new DropLog(player, victim.getDefinition(), item)));
					if(drop.getChance() == Chance.EXTREMELY_RARE && val > 5_000_000) {//5m drop+
						World.get().message(player.getFormatUsername() + " just got an extremely rare drop: " + item.getDefinition().getName());
					}
				}
				rare = false;
			} else if(dropDynamic && !drop.isRare()) {
				if(drop.roll(random) && amount++ <= GameConstants.DROP_THRESHOLD)
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