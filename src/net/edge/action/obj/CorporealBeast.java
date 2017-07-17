package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class CorporealBeast extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(player.getPosition().getX() == 2970) {//entering
					player.move(new Position(2974, player.getPosition().getY(), 2));
				} else {
					player.move(new Position(2970, player.getPosition().getY(), 2));
				}
				return true;
			}
		};
		l.registerFirst(38811);
	}
}
