package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.content.scoreboard.ScoreboardManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.GameObject;

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
