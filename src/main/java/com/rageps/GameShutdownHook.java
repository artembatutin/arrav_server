package com.rageps;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rageps.command.impl.BugCommand;
import com.rageps.content.market.MarketItem;
import com.rageps.content.scoreboard.ScoreboardManager;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The shut down hook which executes any last modifications before the
 * server is shut down.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GameShutdownHook extends Thread {
	
	/**
	 * The {@link ExecutorService} that will execute exit tasks.
	 */
	private final ListeningExecutorService exit;
	
	public GameShutdownHook() {
		ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("RageShutdownHook").build());
		exit = MoreExecutors.listeningDecorator(delegateService);
	}
	
	@Override
	public void run() {
		try {
			exit.submit(() -> {
				for(Player p : World.get().getPlayers()) {
					if(p == null)
						continue;
					World.get().getPersistenceManager().save(p);
				}
			});
			exit.submit(() -> {
				HostManager.serialize(HostListType.BANNED_MAC);
				HostManager.serialize(HostListType.BANNED_IP);
				HostManager.serialize(HostListType.MUTED_IP);
				HostManager.serialize(HostListType.STARTER_RECEIVED);
				
			});
			exit.submit(() -> {
				//todo save clans
				ScoreboardManager.get().serializeIndividualScoreboard();
				MarketItem.serializeMarketItems();
			});
			exit.submit(() -> {
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./bugs.txt", true));
					for(String b : BugCommand.REPORT_LINES) {
						out.write(b + "");
						out.newLine();
					}
					out.close();
				} catch(Exception ignored) {
				}
			});
			
			exit.shutdown();
			exit.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			TimeUnit.SECONDS.sleep(10);
			//this is necessary, otherwise the thread is closed without
			//saving all character files properly.
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}