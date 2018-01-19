package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.object.GameObject;

public class ViewingOrb extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.setViewingOrb(new net.arrav.content.object.ViewingOrb(player, new Position(2398, 5150), new Position(2384, 5157), new Position(2409, 5158), new Position(2388, 5138), new Position(2411, 5137)));
				player.getViewingOrb().open();
				return true;
			}
		};
		l.registerFirst(9391);
	}
}
