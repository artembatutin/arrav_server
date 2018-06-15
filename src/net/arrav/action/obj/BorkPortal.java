package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

import static net.arrav.content.teleport.TeleportType.NORMAL;

public class BorkPortal extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction open = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3087, 3505, 0), NORMAL);
				return true;
			}
		};
		open.registerFirst(29537);
	}
}
