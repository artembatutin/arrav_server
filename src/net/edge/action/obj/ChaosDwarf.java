package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import static net.edge.content.teleport.TeleportType.NORMAL;

public class ChaosDwarf extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(1487, 4704)))
					player.teleport(new Position(3085, 3513, 0), NORMAL);
				return true;
			}
		};
		l.registerFirst(87012);
	}
}
