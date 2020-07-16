package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.action.ActionInitializer;
import com.rageps.net.packet.out.SendConfig;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;

public class SettingsButton extends ActionInitializer {

	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getMovementQueue().isRunning()) {
					player.getMovementQueue().setRunning(false);
				} else {
					if(player.playerData.getRunEnergy() <= 0) {
						return true;
					}
					player.getMovementQueue().setRunning(true);
				}
				return true;
			}
		};
		e.register(153);
		e.register(152);
		e.register(74214);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Boolean acceptAid = player.getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID);
				if(!acceptAid) {
					player.message("Accept aid has been turned on.");
					player.getAttributeMap().reset(PlayerAttributes.ACCEPT_AID);
				} else {
					player.message("Accept aid has been turned off.");
					player.getAttributeMap().set(PlayerAttributes.ACCEPT_AID, false);
				}
				player.out(new SendConfig(427, !acceptAid ? 0 : 1));
				return true;
			}
		};
		e.register(100237);
		e.register(48176);
	}

}
