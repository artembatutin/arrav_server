package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.scoreboard.ScoreboardManager;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

public class Scoreboard extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction view = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				ScoreboardManager.get().sendPlayerScoreboardStatistics(player);
				return true;
			}
		};
		view.registerFirst(30205);
	}
}
