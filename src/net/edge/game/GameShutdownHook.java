package net.edge.game;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.edge.content.commands.impl.BugCommand;
import net.edge.content.market.MarketItem;
import net.edge.net.packet.in.NpcInformationPacket;
import net.edge.world.World;
import net.edge.world.node.actor.npc.drop.NpcDrop;
import net.edge.world.node.actor.npc.drop.NpcDropManager;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.PlayerSerialization;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
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
		ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("EdgevilleShutdown").build());
		exit = MoreExecutors.listeningDecorator(delegateService);
	}
	
	@Override
	public void run() {
		try {
			exit.submit(() -> {
				Player p;
				Iterator<Player> it = World.get().getPlayers().iterator();
				while((p = it.next()) != null) {
					new PlayerSerialization(p).serialize();
				}
			});
			exit.submit(() -> {
				World.getClanManager().save();
				World.getScoreboardManager().serializeIndividualScoreboard();
				MarketItem.serializeMarketItems();
			});
			exit.submit(() -> {
				//drops
				NpcDropManager.serializeDrops();
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./data/suggested_drops.txt", true));
					for(NpcDrop d : NpcInformationPacket.SUGGESTED) {
						out.write(d.toString());
						out.newLine();
					}
					NpcInformationPacket.SUGGESTED.clear();
					out.close();
				} catch(Exception ignored) { }
			});
			exit.submit(() -> {
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./bugs.txt", true));
					for(String b : BugCommand.REPORT_LINES) {
						out.write(b+"");
						out.newLine();
					}
					out.close();
				} catch(Exception ignored) { }
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