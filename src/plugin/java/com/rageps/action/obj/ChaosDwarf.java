package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class ChaosDwarf extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(1487, 4704)))
					player.teleport(new Position(3085, 3513, 0), TeleportType.NORMAL);
				return true;
			}
		};
		l.registerFirst(87012);
	}
}
