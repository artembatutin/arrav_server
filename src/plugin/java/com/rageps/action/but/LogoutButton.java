package com.rageps.action.but;

import com.rageps.GameConstants;
import com.rageps.action.impl.ButtonAction;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.action.ActionInitializer;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

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
				player.send(new LogoutPacket());
				return true;
			}
		};
		e.register(2458);
	}

}
