package net.edge.event.obj;

import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public class Scoreboard extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent view = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				World.getScoreboardManager().sendPlayerScoreboardStatistics(player);
				return true;
			}
		};
		view.registerFirst(30205);
	}
}
