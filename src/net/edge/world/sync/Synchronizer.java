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
	 * The pre-update of preparing players and mobs.
	 *
	 * @param players players list.
	 * @param mobs    the mobs list.
	 */
	public void preUpdate(ActorList<Player> players, ActorList<Mob> mobs) {
		//long time = System.currentTimeMillis();
		for(Player p : players) {
			if(p == null) continue;
			try {
				p.preUpdate();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		for(Mob m : mobs) {
			if(m == null) continue;
			try {
				m.preUpdate();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

//		int pCount = players.size();
//		phaser.bulkRegister(pCount);
//		for(Player player : players) {
//			if(player == null)
//				continue;
//			pCount--;
//			executor.submit(() -> {
//				try {
//					player.preUpdate();
//				} finally {
//					phaser.arriveAndDeregister();
//				}
//			});
//		}
//		while(pCount > 0) {
//			pCount--;
//			phaser.arriveAndDeregister();
//		}
//		phaser.arriveAndAwaitAdvance();
		//System.out.println("[PRE-PLAYER]: " + (System.currentTimeMillis() - time));

		//time = System.currentTimeMillis();
//		int mCount = mobs.size();
//		phaser.bulkRegister(mCount);
//		for(Mob mob : mobs) {
//			if(mob == null)
//				continue;
//			mCount--;
//			executor.submit(() -> {
//				try {
//					mob.preUpdate();
//				} finally {
//					phaser.arriveAndDeregister();
//				}
//			});
//		}
//		while(mCount > 0) {
//			mCount--;
//			phaser.arriveAndDeregister();
//		}
//		phaser.arriveAndAwaitAdvance();
		//System.out.println("[PRE-NPC]: " + (System.currentTimeMillis() - time));
	}

	/**
	 * The main tick update for players.
	 *
	 * @param players players list.
	 */
	public void update(ActorList<Player> players) {
		//long time = System.currentTimeMillis();
		int pCount = players.size();
		phaser.bulkRegister(pCount);
		for(Player player : players) {
			if(player == null)
				continue;
			pCount--;
			executor.submit(() -> {
				try {
					player.update();
				} finally {
					phaser.arriveAndDeregister();
				}
			});
		}
		while(pCount > 0) {
			pCount--;
			phaser.arriveAndDeregister();
		}
		phaser.arriveAndAwaitAdvance();
		//System.out.println("[SYNC]: " + (System.currentTimeMillis() - time));
	}

	/**
	 * The post-update process of resetting players and mobs.
	 *
	 * @param players players list.
	 * @param mobs    mobs list.
	 */
	public void postUpdate(ActorList<Player> players, ActorList<Mob> mobs) {
		//long time = System.currentTimeMillis();
		for(Player player : players) {
			if(player == null)
				continue;
			player.postUpdate();
		}
		//System.out.println("[POST-PLAYER]: " + (System.currentTimeMillis() - time));

		//time = System.currentTimeMillis();
		for(Mob mob : mobs) {
			if(mob == null)
				continue;
			mob.postUpdate();
		}
		//System.out.println("[POST-NPC]: " + (System.currentTimeMillis() - time));
	}

}
