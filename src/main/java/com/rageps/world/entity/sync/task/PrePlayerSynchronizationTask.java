package com.rageps.world.entity.sync.task;

import com.rageps.world.entity.actor.player.Player;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified {@link Player}.
 *
 * @author Graham
 * @author Major
 */
public final class PrePlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The player.
	 */
	private final Player player;


	/**
	 * Creates the {@link PrePlayerSynchronizationTask} for the specified {@link Player}.
	 *
	 * @param player The Player.
	 */
	public PrePlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		player.preUpdate();
	}

}