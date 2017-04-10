package net.edge.world.model.node.synchronizer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.edge.world.World;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.npc.NpcUpdater;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.PlayerUpdater;
import net.edge.world.model.node.item.ItemNode;
import net.edge.utils.LoggerUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Synchronizes all of the {@link Player}s and {@link Npc}s with the {@link World} through the updating protocol. The entire
 * process except for pre-synchronization is done in parallel, effectively utilizing as much of the host computer's CPU as
 * possible for maximum performance.
 * @author lare96 <http://github.org/lare96>
 */
public final class WorldSynchronizer {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = LoggerUtils.getLogger(WorldSynchronizer.class);
	
	/**
	 * A synchronization barrier that will ensure the main game thread waits for the {@code updateExecutor} threads to finish
	 * executing {@link EntitySynchronization}s before proceeding.
	 */
	private final Phaser synchronizer = new Phaser(1);
	
	/**
	 * An {@link ExecutorService} that will execute {@link EntitySynchronization}s in parallel.
	 */
	private final ExecutorService updateExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("WorldSynchronizerThread").build());
	
	/**
	 * The regional tick counter for processing such as {@link ItemNode} in a region.
	 */
	private int regionalTick;
	
	/**
	 * Pre-synchronization, update the walking queue and perform miscellaneous processing that requires cyclic execution.
	 * This is <strong>generally</strong> not safe to do in parallel.
	 */
	public void preSynchronize() {
		for(Player it : World.getPlayers()) {
			try {
				it.getSession().dequeue();
				it.getMovementQueue().sequence();
				it.sequence();
				
			} catch(Exception e) {
				World.queueLogout(it);
				LOGGER.log(Level.WARNING, "Couldn't pre sync player " + it.toString(), e);
			}
		}
		for(Npc it : World.getNpcs()) {
			if(it.isActive()) {
				try {
					it.sequence();
					it.getMovementQueue().sequence();
				} catch(Exception e) {
					LOGGER.log(Level.WARNING, "Couldn't pre sync npc " + it.toString(), e);
				}
			}
		}
	}
	
	/**
	 * Synchronization, send the {@link Player} and {@link Npc} updating messages for all online {@code Player}s in
	 * parallel.
	 */
	public void synchronize() {
		synchronizer.bulkRegister(World.getPlayers().size());
		World.getPlayers().forEach(it -> updateExecutor.execute(new EntitySynchronization(this, it) {
			@Override
			public void execute() {
				try {
					it.getSession().queue(new PlayerUpdater().write(it));
					it.getSession().queue(new NpcUpdater().write(it));
				} catch(Exception e) {
					World.queueLogout(it);
					LOGGER.log(Level.WARNING, "Couldn't sync player " + it.toString(), e);
				}
			}
		}));
		synchronizer.arriveAndAwaitAdvance();
	}
	
	/**
	 * Post-synchronization, clear various flags. This can be done safely in parallel.
	 */
	public void postSynchronize() {
		for(Player it : World.getPlayers()) {
			it.getSession().flushQueue();
			it.reset();
			it.setCachedUpdateBlock(null);
		}
		/*synchronizer.bulkRegister(World.getPlayers().size());
		World.getPlayers().forEach(it -> updateExecutor.execute(new EntitySynchronization(this, it) {
			@Override
			public void execute() {
				it.getSession().flushQueue();
				it.reset();
				it.setCachedUpdateBlock(null);
			}
		}));
		synchronizer.arriveAndAwaitAdvance();*/
		
		for(Npc it : World.getNpcs()) {
			it.reset();
		}
		/*synchronizer.bulkRegister(World.getNpcs().size());
		World.getNpcs().forEach(it -> updateExecutor.execute(new EntitySynchronization(this, it) {
			@Override
			public void execute() {
				it.reset();
			}
		}));
		synchronizer.arriveAndAwaitAdvance();*/
		
		regionTick();
	}
	
	public void regionTick() {
		//Regional tick.
		regionalTick++;
		if(regionalTick == 10) {
			World.getRegions().getRegions().forEach((i, it) -> it.sequence());
			regionalTick = 0;
		}
	}
	
	/**
	 * Gets the {@link Phaser} synchronizer.
	 * @return synchronizer.
	 */
	public Phaser getSynchronizer() {
		return synchronizer;
	}
	
}