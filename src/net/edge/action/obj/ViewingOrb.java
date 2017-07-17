package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class ViewingOrb extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.setViewingOrb(new net.edge.content.ViewingOrb(player, new Position(2398, 5150), new Position(2384, 5157), new Position(2409, 5158), new Position(2388, 5138), new Position(2411, 5137)));
				player.getViewingOrb().open();
				return true;
			}
		};
		l.registerFirst(9391);
	}
}
