package com.rageps.action.but;

import com.rageps.GameConstants;
import com.rageps.action.impl.ButtonAction;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.item.Item;

public class PanelIronManButton extends ActionInitializer {
	
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
				player.getAttributeMap().set(PlayerAttributes.INTRODUCTION_STAGE, 3);
				player.getActivityManager().enable();
				return true;
			}
		};
		e.register(200);
		e.register(201);
		
	}
	
}
