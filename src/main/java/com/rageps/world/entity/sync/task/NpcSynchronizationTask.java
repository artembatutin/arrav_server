package com.rageps.world.entity.sync.task;

import com.rageps.net.refactor.packet.out.model.update.NpcSynchronizationPacket;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.Region;
import com.rageps.world.entity.sync.seg.AddNpcSegment;
import com.rageps.world.entity.sync.seg.MovementSegment;
import com.rageps.world.entity.sync.seg.RemoveMobSegment;
import com.rageps.world.entity.sync.seg.SynchronizationSegment;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A {@link SynchronizationTask} which synchronizes npcs with the specified {@link Player}.
 *
 * @author Major
 */
public final class NpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum amount of local npcs.
	 */
	private static final int MAXIMUM_LOCAL_NPCS = 255;

	/**
	 * The maximum number of npcs to load per cycle. This prevents the update packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
	 */
	private static final int NEW_NPCS_PER_CYCLE = 20;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link NpcSynchronizationTask} for the specified player.
	 *
	 * @param player The player.
	 */
	public NpcSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {

		List<SynchronizationSegment> segments = new ArrayList<>();




		Set<Mob> locals = player.getLocalMobs();


//
		int originalCount = locals.size();
		final Position playerPosition = player.getPosition();

		Iterator<Mob> $it = locals.iterator();

		while ($it.hasNext()) {
			Mob mob = $it.next();
			if(mob.getState() == EntityState.ACTIVE && mob.isVisible() && player.getInstance() == mob.getInstance() && mob.getPosition().isViewableFrom(player.getPosition()) && !mob.isNeedsPlacement()) {
				segments.add(new MovementSegment(mob.getBlockSet(), mob.getDirections()));

			} else {
				segments.add(new RemoveMobSegment());
				$it.remove();
			}
		}



			int added = 0;
		Region r = player.getRegion();
		if(r != null) {
			processMobs(r, player, added, segments);
			ObjectList<Region> surrounding = r.getSurroundingRegions();
			if(surrounding != null) {
				for(Region s : surrounding) {
					processMobs(s, player, added, segments);
				}
			}
		}
		player.send(new NpcSynchronizationPacket(playerPosition, segments, originalCount));
	}

	private void processMobs(Region region, Player player, int added, List<SynchronizationSegment> segments) {
		if(region != null) {
			if(!region.getMobs().isEmpty()) {
				for(Mob mob : region.getMobs()) {
					if(added == 15 || player.getLocalMobs().size() >= 255)
						break;
					if(mob == null)
						continue;
					if(mob.getState() != EntityState.ACTIVE)
						continue;
					if(mob.getInstance() != player.getInstance())
						continue;
					if(mob.isVisible() && mob.getPosition().isViewableFrom(player.getPosition())) {
						if(player.getLocalMobs().add(mob)) {
							//todo should we be facing it?
							segments.add(new AddNpcSegment(mob.getBlockSet(), mob.getSlot(), mob.getPosition(), mob.getId()));
							added++;
						}
					}
				}
			}
		}
	}




}