package com.rageps.world.entity.sync.task;

import com.rageps.world.entity.actor.mob.Mob;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified npc.
 *
 * @author Major
 */
public final class PreNpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The npc.
	 */
	private final Mob npc;

	/**
	 * Creates the {@link PreNpcSynchronizationTask} for the specified npc.
	 *
	 * @param npc The npc.
	 */
	public PreNpcSynchronizationTask(Mob npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		//npc.getWalkingQueue().pulse();
		npc.preUpdate();
	}

}