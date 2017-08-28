package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

public class AgilityStairs extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(2532, 3545, 1))) ;
//						player.teleport(new Position(2532, 3546, 0), LADDER); TODO: add teleports
				return true;
			}
		};
		l.registerFirst(3205);

	}
}
