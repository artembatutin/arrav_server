package net.arrav;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.arrav.content.clanchat.ClanManager;
import net.arrav.content.commands.impl.BugCommand;
import net.arrav.content.market.MarketItem;
import net.arrav.content.scoreboard.ScoreboardManager;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.net.packet.in.MobInformationPacket;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.actor.mob.drop.DropManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.PlayerSerialization;

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
	
	GameShutdownHook() {
		ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("ArravShutdownHook").build());
		exit = MoreExecutors.listeningDecorator(delegateService);
	}
	
	@Override
	public void run() {
		try {
			exit.submit(() -> {
				for(Player p : World.get().getPlayers()) {
					if(p == null)
						continue;
					new PlayerSerialization(p).serialize();
				}
			});
			exit.submit(() -> {
				HostManager.serialize(HostListType.BANNED_MAC);
				HostManager.serialize(HostListType.BANNED_IP);
				HostManager.serialize(HostListType.MUTED_IP);
				HostManager.serialize(HostListType.STARTER_RECEIVED);
				
			});
			exit.submit(() -> {
				ClanManager.get().save();
				ScoreboardManager.get().serializeIndividualScoreboard();
				MarketItem.serializeMarketItems();
			});
			exit.submit(() -> {
				//drops
				DropManager.serializeDrops();
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./data/suggested_drops.txt", true));
					for(Drop d : MobInformationPacket.SUGGESTED) {
						out.write(d.toString());
						out.newLine();
					}
					MobInformationPacket.SUGGESTED.clear();
					out.close();
				} catch(Exception ignored) {
				}
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