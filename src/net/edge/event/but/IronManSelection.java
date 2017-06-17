package net.edge.event.but;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.game.GameConstants;
import net.edge.net.PunishmentHandler;
import net.edge.world.node.entity.player.Player;

public class IronManSelection extends EventInitializer {
	
	@Override
	public void init() {
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				boolean iron = button == 100;
				if(iron)
					player.setIron(1);
				else
					player.setIron(0);
				player.getActivityManager().enable();
				player.sendDefaultSidebars();
				player.getMessages().sendCloseWindows();
				if(!PunishmentHandler.recievedStarter(player.getSession().getHost() + "-" + (iron ? "iron" : "reg"))) {
					if(iron)
						player.getInventory().setItems(GameConstants.IRON_STARTER);
					else
						player.getInventory().setItems(GameConstants.REGULAR_STARTER);
					player.getInventory().refresh(player);
					PunishmentHandler.addStarter(player.getSession().getHost() + "-" + (iron ? "iron" : "reg"));
				} else {
					player.message("You already received a starter package before.");
				}
				player.getAttr().get("introduction_stage").set(3);
				return true;
			}
		};
		e.register(100);
		e.register(101);

	}
	
}
