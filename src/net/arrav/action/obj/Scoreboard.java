package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.content.scoreboard.ScoreboardManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;

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
