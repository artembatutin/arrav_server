package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

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
