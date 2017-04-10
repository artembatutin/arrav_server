package net.edge.world.model.node.synchronizer;

import net.edge.world.World;
import net.edge.world.model.node.entity.EntityNode;

/**
 * A {@code Runnable} implementation that executes some sort of logic while handling thread-safety for a {@link EntityNode}, {@code Exception}s, as well as synchronization barriers.
 */
abstract class EntitySynchronization implements Runnable {
	
	/**
	 * The main synchronizer in charge of the process.
	 */
	private WorldSynchronizer worldSynchronizer;
	
	/**
	 * The {@link EntityNode} to synchronize over.
	 */
	private final EntityNode entity;
	
	/**
	 * Creates a new {@link EntitySynchronization}.
	 * @param entity The {@link EntityNode} to synchronize over.
	 */
	EntitySynchronization(WorldSynchronizer worldSynchronizer, EntityNode entity) {
		this.worldSynchronizer = worldSynchronizer;
		this.entity = entity;
	}
	
	@Override
	public void run() {
		synchronized(entity) {
			try {
				execute();
			} catch(Exception e) {
				e.printStackTrace();
				remove();
			} finally {
				worldSynchronizer.getSynchronizer().arriveAndDeregister();
			}
		}
	}
	
	/**
	 * The logic to execute within this task.
	 */
	public abstract void execute();
	
	/**
	 * Removes the {@code entity} if an {@code Exception} is thrown.
	 */
	private void remove() {
		if(entity.isPlayer()) {
			World.queueLogout(entity.toPlayer());
		} else if(entity.isNpc()) {
			World.getNpcs().remove(entity.toNpc());
		} else {
			throw new IllegalStateException("should never reach here");
		}
	}
}
