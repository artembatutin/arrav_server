package com.rageps.world.entity.sync.task;

import com.rageps.world.entity.actor.mob.Mob;

/**
 * A {@link SynchronizationTask} which does post-synchronization work for the specified {@link Npc}.
 *
 * @author Major
 */
public final class PostNpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The npc.
	 */
	private final Mob npc;

	/**
	 * Creates the {@link PostNpcSynchronizationTask} for the specified player.
	 *
	 * @param npc The npc.
	 */
	public PostNpcSynchronizationTask(Mob npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		npc.postUpdate();
	}

}