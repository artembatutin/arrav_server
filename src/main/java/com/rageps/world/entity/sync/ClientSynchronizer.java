package com.rageps.world.entity.sync;

import com.rageps.world.entity.actor.ActorList;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

/**
 * The {@link ClientSynchronizer} manages the update sequence which keeps clients synchronized with the in-game world.
 * There are two implementations distributed with Apollo: {@link SequentialClientSynchronizer} which is optimized for a
 * single-core/single-processor machine and {@link ParallelClientSynchronizer} which is optimized for a multi-processor/
 * multi-core machines.
 * <p>
 * To switch between the two synchronizer implementations, edit the {@code synchronizers.xml} configuration file. The
 * default implementation is currently {@link ParallelClientSynchronizer} as the vast majority of machines today have
 * two or more cores.
 *
 * @author Graham
 */
public abstract class ClientSynchronizer {

	/**
	 * Synchronizes the state of the clients with the state of the server.
	 *
	 * @param players The {@link ActorList} containing the {@link Player}s.
	 * @param npcs The {@link ActorList} containing the {@link Mob}s.
	 */
	public abstract void synchronize(ActorList<Player> players, ActorList<Mob> npcs);

}