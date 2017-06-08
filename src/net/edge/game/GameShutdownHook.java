package net.edge.game;

import net.edge.content.market.MarketItem;
import net.edge.net.packet.impl.NpcInformationPacket;
import net.edge.world.World;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

/**
 * The shut down hook which executes any last modifications before the
 * server is shut down.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GameShutdownHook extends Thread {
	
	@Override
	public void run() {
		try {
			for(Player p : World.get().getPlayers()) {
				new PlayerSerialization(p).serialize();
			}
			World.getClanManager().save();
			World.getScoreboardManager().serializeIndividualScoreboard();
			MarketItem.serializeMarketItems();
			
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
			
			TimeUnit.SECONDS.sleep(5);
			//this is necessary, otherwise the thread is closed without
			//saving all character files properly.
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}