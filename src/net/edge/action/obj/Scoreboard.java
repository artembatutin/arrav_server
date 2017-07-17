package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class Scoreboard extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction view = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				World.getScoreboardManager().sendPlayerScoreboardStatistics(player);
				return true;
			}
		};
		view.registerFirst(30205);
	}
}
