package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.net.packet.out.SendConfig;
import net.arrav.world.entity.actor.player.Player;

public class SettingsButton extends ActionInitializer {

	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getMovementQueue().isRunning()) {
					player.getMovementQueue().setRunning(false);
				} else {
					if(player.getRunEnergy() <= 0) {
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
				Boolean acceptAid = (Boolean) player.getAttr().get("accept_aid").get();
				if(!acceptAid) {
					player.message("Accept aid has been turned on.");
					player.getAttr().get("accept_aid").set(true);
				} else {
					player.message("Accept aid has been turned off.");
					player.getAttr().get("accept_aid").set(false);
				}
				player.out(new SendConfig(427, !acceptAid ? 0 : 1));
				return true;
			}
		};
		e.register(100237);
		e.register(48176);
	}

}
