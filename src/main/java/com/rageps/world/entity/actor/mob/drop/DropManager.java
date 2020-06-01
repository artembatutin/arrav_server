package com.rageps.world.entity.actor.mob.drop;

import com.rageps.content.skill.Skills;
import com.rageps.content.skill.prayer.Bone;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * The static-utility class that manages all of the {@link DropTable}s
 * including the process of dropping the items when an {@link Mob} is killed.
 * @author lare96 <http://github.org/lare96>
 * @author Tamatea <tamateea@gmail.com>
 */
public final class DropManager {
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Mob}s.
	 */
	public static ObjectArrayList<DropTable> TABLES = new ObjectArrayList<>();
	
	/**
	 * Drops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * @param killer the killer, may or may not exist.
	 * @param victim the victim that was killed.
	 */
	public static void dropItems(Player killer, Mob victim) {
		Optional<DropTable> table = getDroptable(victim.getId());
		if(!table.isPresent()) {
			return;
		}
		Position pos = victim.getPosition();
		if(victim.getId() == 3847) {//sea troll
			pos = new Position(2346, 3700);
		}
		final Position p = pos;
		Region r = victim.getRegion();
		if(r != null) {
			List<Item> dropItems = table.get().toItems(killer, false);
			for(Item drop : dropItems) {
				if(drop == null)
					continue;

				if(killer.getInventory().contains(18337)) {
					Optional<Bone> bone = Bone.getBone(drop.getId());
					bone.ifPresent(b -> Skills.experience(killer, b.getExperience() * 1.5, Skills.PRAYER));
					if(bone.isPresent())
						continue;
				}
				//collectors necklace goes here.
				r.register(new GroundItem(drop, p, killer));
			}
		}
	}

	public static Optional<DropTable> getDroptable(int npcId)  {
		for(DropTable table : TABLES) {
			if(table.getNpcId() == npcId)
				return Optional.of(table);
		}
		return Optional.empty();
	}
}