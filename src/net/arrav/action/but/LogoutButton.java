package net.arrav.action.but;

import net.arrav.GameConstants;
import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.minigame.MinigameHandler;
import net.arrav.net.packet.out.SendLogout;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.concurrent.TimeUnit;

public class LogoutButton extends ActionInitializer {

	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(!MinigameHandler.execute(player, t -> t.canLogout(player)))
					return true;
				if(!player.getCombat().hasPassed(GameConstants.COMBAT_SECONDS)) {
					player.message("You must wait " + (TimeUnit.MILLISECONDS.toSeconds(10_000 - player.getCombat().elapsedTime())) + " seconds after combat before logging out.");
					return true;
				}
				if(player.getActivityManager().contains(ActivityManager.ActivityType.LOG_OUT)) {
					player.message("You can't log out right now.");
					return true;
				}
				player.out(new SendLogout());
				return true;
			}
		};
		e.register(9154);
	}

}
