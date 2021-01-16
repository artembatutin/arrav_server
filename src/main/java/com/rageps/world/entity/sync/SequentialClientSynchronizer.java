package com.rageps.world.entity.sync;



import com.rageps.service.impl.GameService;
import com.rageps.world.entity.actor.ActorList;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.sync.task.*;

import java.util.HashMap;
import java.util.Set;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a single thread (the {@link GameService} thread from
 * which this is called). Each client is processed sequentially. Therefore this class will work well on machines with a
 * single core/processor. The {@link ParallelClientSynchronizer} will work better on machines with multiple
 * cores/processors, however, both classes will work.
 *
 * @author Graham
 * @author Major
 */
public final class SequentialClientSynchronizer extends ClientSynchronizer {

	@Override
	public void synchronize(ActorList<Player> players, ActorList<Mob> npcs) {
		//Map<RegionCoordinates, Set<RegionUpdateMessage>> encodes = new HashMap<>(), updates = new HashMap<>();

		for (Player player : players) {
			SynchronizationTask task = new PrePlayerSynchronizationTask(player);
			task.run();
		}

		for (Mob npc : npcs) {
			SynchronizationTask task = new PreNpcSynchronizationTask(npc);
			task.run();
		}

		for (Player player : players) {
			SynchronizationTask task = new PlayerSynchronizationTask(player);
			task.run();
			task = new NpcSynchronizationTask(player);
			task.run();
		}

		for (Player player : players) {
			SynchronizationTask task = new PostPlayerSynchronizationTask(player);
			task.run();
		}

		for (Mob npc : npcs) {
			SynchronizationTask task = new PostNpcSynchronizationTask(npc);
			task.run();
		}
	}

}