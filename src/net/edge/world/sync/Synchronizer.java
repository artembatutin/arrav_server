package net.edge.world.sync;

import net.edge.net.packet.out.SendLogout;
import net.edge.util.LoggerUtils;
import net.edge.world.node.entity.EntityList;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcUpdater;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerUpdater;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Synchronizer {
	
	/**
	 * Logging issues.
	 */
	private Logger logger = LoggerUtils.getLogger(this.getClass());
	
	/**
	 * Executor service for parallel updating.
	 */
	private final ExecutorService synchronizer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	public void synchronize(EntityList<Player> players, EntityList<Npc> npcs, int playerSize) {
		// pre-synchronization
		//time = System.currentTimeMillis();
		for(Player player : players) {
			if(player == null)
				continue;
			try {
				if(player.active()) {
					player.update();
				}
			} catch(Exception e) {
				logger.log(Level.WARNING, "Pre sync player " + player, e);
				player.out(new SendLogout());
			}
		}
		//System.out.println("[PRE-PLAYER]: " + (System.currentTimeMillis() - time));
		//time = System.currentTimeMillis();
		for(Npc npc : npcs) {
			if(npc == null)
				continue;
			try {
				if(npc.active()) {
					npc.update();
					npc.getMovementQueue().sequence();
				}
			} catch(Exception e) {
				logger.log(Level.WARNING, "Pre sync npc " + npc, e);
				e.printStackTrace();
			}
		}
		//System.out.println("[PRE-NPC]: " + (System.currentTimeMillis() - time));
		
		
		// Synchronization
		//time = System.currentTimeMillis();
		CountDownLatch latch = new CountDownLatch(playerSize);
		for(Player player : players) {
			if(player == null)
				continue;
			synchronizer.submit(new SynchronizerTask(player, latch));
		}
		try {
			latch.await();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		// Post synchronization
		//time = System.currentTimeMillis();
		for(Player player : players) {
			if(player == null)
				continue;
			if(player.isHuman()) {
				player.getSession().flushQueue();
			}
			player.reset();
			player.setCachedUpdateBlock(null);
			player.checkRemoval();
		}
		//System.out.println("[POST-PLAYER]: " + (System.currentTimeMillis() - time));
		//time = System.currentTimeMillis();
		for(Npc npc : npcs) {
			if(npc == null)
				continue;
			try {
				npc.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println("[POST-NPC]: " + (System.currentTimeMillis() - time));
		
	}
	
	/**
	 * A model that applies the update procedure within a synchronization block.
	 */
	private final class SynchronizerTask implements Runnable {
		
		/**
		 * The player.
		 */
		private final Player player;
		
		private final CountDownLatch latch;
		
		/**
		 * Creates a new {@link SynchronizerTask}.
		 *
		 * @param player The player.
		 */
		private SynchronizerTask(Player player, CountDownLatch latch) {
			this.player = player;
			this.latch = latch;
		}
		
		@Override
		public void run() {
			synchronized(player) {
				try {
					PlayerUpdater.write(player);
					NpcUpdater.write(player);
					player.getSession().pollOutgoingMessages();
				} catch(Exception e) {
					logger.log(Level.WARNING, "Sync player " + player, e);
					player.out(new SendLogout());
				} finally {
					latch.countDown();
				}
			}
		}
	}
}
