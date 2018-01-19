package net.arrav.action.but;

import net.arrav.GameConstants;
import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

public class IronManSelection extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				boolean iron = button == 200;
				if(iron)
					player.setIron(1, true);
				else
					player.setIron(0, true);
				player.getActivityManager().enable();
				player.sendDefaultSidebars();
				player.closeWidget();
				player.getInventory().clear(false);
				if(iron) {
					player.getInventory().fillItems(GameConstants.IRON_STARTER);
				} else if(player.firstLogin) {
					player.getInventory().fillItems(GameConstants.REGULAR_STARTER);
					HostManager.add(player, HostListType.STARTER_RECEIVED);
				} else {
					player.getInventory().clear(false);
					player.getInventory().add(new Item(995, 500000), 0, false);
					player.getInventory().updateBulk();
					player.message("You already received your regular starter package before.");
				}
				player.getInventory().updateBulk();
				player.getAttr().get("introduction_stage").set(3);
				player.getActivityManager().enable();
				return true;
			}
		};
		e.register(200);
		e.register(201);
		
	}
	
}
