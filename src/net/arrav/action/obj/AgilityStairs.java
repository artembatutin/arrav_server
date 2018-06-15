package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

import static net.arrav.content.teleport.TeleportType.LADDER;

public class AgilityStairs extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(2532, 3545, 1)))
					player.teleport(new Position(2532, 3546, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(3205);
		
	}
}
