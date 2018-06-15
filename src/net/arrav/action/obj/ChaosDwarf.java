package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

import static net.arrav.content.teleport.TeleportType.NORMAL;

public class ChaosDwarf extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(1487, 4704)))
					player.teleport(new Position(3085, 3513, 0), NORMAL);
				return true;
			}
		};
		l.registerFirst(87012);
	}
}
