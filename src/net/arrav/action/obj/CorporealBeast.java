package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.locale.Position;

public class CorporealBeast extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
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
