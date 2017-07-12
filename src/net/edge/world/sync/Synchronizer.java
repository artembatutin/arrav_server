package net.edge.world.sync;

import net.edge.net.packet.out.SendLogout;
import net.edge.util.LoggerUtils;
import net.edge.world.node.entity.EntityList;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.edge.world.sync.UpdateType.POST_UPDATE;
import static net.edge.world.sync.UpdateType.PRE_UPDATE;
import static net.edge.world.sync.UpdateType.UPDATE;

public class Synchronizer {
	
	private final Phaser phaser = new Phaser(1);
	
	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	/**
	 * The pre-update of preparing players and npcs.
	 * @param players players list.
	 * @param npcs the npcs list.
	 */
	public void preUpdate(EntityList<Player> players, EntityList<Npc> npcs) {
		//long time = System.currentTimeMillis();
		phaser.bulkRegister(players.size());
		for(Player player : players) {
			if(player == null)
				continue;
			executor.submit(new UpdateTask(PRE_UPDATE, player, phaser));
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[PRE-PLAYER]: " + (System.currentTimeMillis() - time));
		
		
		//time = System.currentTimeMillis();
		phaser.bulkRegister(npcs.size());
		for(Npc npc : npcs) {
			if(npc == null)
				continue;
			executor.submit(new UpdateTask(PRE_UPDATE, npc, phaser));
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[PRE-NPC]: " + (System.currentTimeMillis() - time));
	}
	
	/**
	 * The main tick update for players.
	 * @param players players list.
	 */
	public void update(EntityList<Player> players) {
		long time = System.currentTimeMillis();
		phaser.bulkRegister(players.size());
		for(Player player : players) {
			if(player == null)
				continue;
			executor.submit(new UpdateTask(UPDATE, player, phaser));
		}
		phaser.arriveAndAwaitAdvance();
		System.out.println("[SYNC]: " + (System.currentTimeMillis() - time));
	}
	
	/**
	 * The post-update process of resetting players and npcs.
	 * @param players players list.
	 * @param npcs npcs list.
	 */
	public void postUpdate(EntityList<Player> players, EntityList<Npc> npcs) {
		//time = System.currentTimeMillis();
		phaser.bulkRegister(players.size());
		for(Player player : players) {
			if(player == null)
				continue;
			executor.submit(new UpdateTask(POST_UPDATE, player, phaser));
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[POST-PLAYER]: " + (System.currentTimeMillis() - time));
		
		
		//time = System.currentTimeMillis();
		phaser.bulkRegister(npcs.size());
		for(Npc npc : npcs) {
			if(npc == null)
				continue;
			executor.submit(new UpdateTask(POST_UPDATE, npc, phaser));
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[POST-NPC]: " + (System.currentTimeMillis() - time));
	}
	
}
