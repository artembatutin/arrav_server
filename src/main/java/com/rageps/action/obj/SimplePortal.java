package com.rageps.action.obj;

import com.rageps.GameConstants;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class SimplePortal extends ActionInitializer {
	@Override
	public void init() {
		//Green portal at home
		ObjectAction portal = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(3005, 3963, 0)))//wilderness agility
					player.teleport(GameConstants.STARTING_POSITION, TeleportType.OBELISK);
				if(object.getPosition().same(new Position(2996, 9823, 0)))//to rune essence
					player.teleport(new Position(2922, 4819, 0), TeleportType.FREEZE);
				if(object.getPosition().same(new Position(2922, 4819, 0)))//from rune essence
					player.teleport(new Position(2996, 9823, 0), TeleportType.FREEZE);
				return true;
			}
		};
		portal.registerFirst(2273);
		portal.registerFirst(44276);
		
	}
}
