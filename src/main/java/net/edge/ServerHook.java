package net.edge;

import net.edge.world.World;
import net.edge.world.content.market.MarketItem;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;

import java.util.concurrent.TimeUnit;

/**
 * The shut down hook which executes any last modifications before the
 * server is shut down.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ServerHook extends Thread {
	
	@Override
	public void run() {
		try {
			for(Player p : World.getPlayers()) {
				if(p != null) {
					World.getService().submit(() -> new PlayerSerialization(p).serialize());
				}
			}
			World.getClanManager().save();
			World.getScoreboardManager().serializeIndividualScoreboard();
			MarketItem.serializeMarketItems();
			
			TimeUnit.SECONDS.sleep(5);
			//this is necessary, otherwise the thread is closed without
			//saving all character files properly.
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
