package com.rageps.world.entity.sync;

import com.rageps.service.impl.GameService;
import com.rageps.util.ThreadUtil;
import com.rageps.world.entity.actor.ActorList;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.sync.task.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a thread pool. A {@link Phaser} is used to ensure that
 * the synchronization is complete, allowing control to return to the {@link GameService} that started the
 * synchronization. This class will scale well with machines that have multiple cores/processors. The
 * {@link SequentialClientSynchronizer} will work better on machines with a single core/processor, however, both
 * classes
 * will work.
 *
 * @author Graham
 * @author Major
 */
public final class ParallelClientSynchronizer extends ClientSynchronizer {

	/**
	 * The ExecutorService.
	 */
	private final ExecutorService executor;

	/**
	 * The Phaser.
	 */
	private final Phaser phaser = new Phaser(1);

	/**
	 * Creates the ParallelClientSynchronizer backed by a thread pool with a number of threads equal to the number of
	 * processing cores available.
	 */
	public ParallelClientSynchronizer() {
		executor = Executors.newFixedThreadPool(ThreadUtil.AVAILABLE_PROCESSORS, ThreadUtil.create("ClientSynchronizer"));
	}

	@Override
	public void synchronize(ActorList<Player> players, ActorList<Mob> npcs) {
		int playerCount = players.size();
		int npcCount = npcs.size();

		//Map<RegionCoordinates, Set<RegionUpdateMessage>> encodes = new ConcurrentHashMap<>();
		//Map<RegionCoordinates, Set<RegionUpdateMessage>> updates = new ConcurrentHashMap<>();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			if(player == null)
				continue;
			SynchronizationTask task = new PrePlayerSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(npcCount);
		for (Mob npc : npcs) {
			if (npc == null)
				continue;
			SynchronizationTask task = new PreNpcSynchronizationTask(npc);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			if(player == null)
				continue;
			SynchronizationTask task = new PlayerSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			if(player == null)
				continue;
			SynchronizationTask task = new NpcSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			if(player == null)
				continue;
			SynchronizationTask task = new PostPlayerSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(npcCount);
		for (Mob npc : npcs) {
			if (npc == null)
				continue;
			SynchronizationTask task = new PostNpcSynchronizationTask(npc);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();
	}

}