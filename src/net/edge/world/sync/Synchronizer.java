package net.edge.world.sync;

import net.edge.world.entity.actor.ActorList;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class Synchronizer {
	
	private final Phaser phaser = new Phaser(1);
	
	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	/**
	 * The pre-update of preparing players and npcs.
	 * @param players players list.
	 * @param npcs the npcs list.
	 */
	public void preUpdate(ActorList<Player> players, ActorList<Mob> npcs) {
		//long time = System.currentTimeMillis();
		phaser.bulkRegister(players.size());
		for(Player player : players) {
			if(player == null)
				continue;
			executor.submit(() -> {
				try {
					player.preUpdate();
				} finally {
					phaser.arriveAndDeregister();
				}
			});
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[PRE-PLAYER]: " + (System.currentTimeMillis() - time));
		
		
		//time = System.currentTimeMillis();
		phaser.bulkRegister(npcs.size());
		for(Mob mob : npcs) {
			if(mob == null)
				continue;
			executor.submit(() -> {
				try {
					mob.preUpdate();
				} finally {
					phaser.arriveAndDeregister();
				}
			});
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[PRE-NPC]: " + (System.currentTimeMillis() - time));
	}
	
	/**
	 * The main tick update for players.
	 * @param players players list.
	 */
	public void update(ActorList<Player> players) {
		long time = System.currentTimeMillis();
		phaser.bulkRegister(players.size());
		for(Player player : players) {
			if(player == null)
				continue;
			executor.submit(() -> {
				try {
					player.update();
				} finally {
					phaser.arriveAndDeregister();
				}
			});
		}
		phaser.arriveAndAwaitAdvance();
		System.out.println("[SYNC]: " + (System.currentTimeMillis() - time));
	}
	
	/**
	 * The post-update process of resetting players and npcs.
	 * @param players players list.
	 * @param npcs npcs list.
	 */
	public void postUpdate(ActorList<Player> players, ActorList<Mob> npcs) {
		//time = System.currentTimeMillis();
		phaser.bulkRegister(players.size());
		for(Player player : players) {
			if(player == null)
				continue;
			executor.submit(() -> {
				try {
					player.postUpdate();
				} finally {
					phaser.arriveAndDeregister();
				}
			});
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[POST-PLAYER]: " + (System.currentTimeMillis() - time));
		
		
		//time = System.currentTimeMillis();
		phaser.bulkRegister(npcs.size());
		for(Mob mob : npcs) {
			if(mob == null)
				continue;
			executor.submit(() -> {
				try {
					mob.postUpdate();
				} finally {
					phaser.arriveAndDeregister();
				}
			});
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[POST-NPC]: " + (System.currentTimeMillis() - time));
	}
	
}
