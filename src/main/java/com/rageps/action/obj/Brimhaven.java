package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class Brimhaven extends ActionInitializer {
	@Override
	public void init() {
		//staires
		ObjectAction s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(2648, 9592)))
					player.teleport(new Position(2642, 9595, 2), TeleportType.LADDER);
				return true;
			}
		};
		s.registerFirst(5094);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(2644, 9593, 2)))
					player.teleport(new Position(2649, 9591, 0), TeleportType.LADDER);
				return true;
			}
		};
		s.registerFirst(5096);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(2635, 9514)))
					player.teleport(new Position(2637, 9510, 2), TeleportType.LADDER);
				return true;
			}
		};
		s.registerFirst(5097);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(2635, 9511, 2)))
					player.teleport(new Position(2637, 9517, 0), TeleportType.LADDER);
				return true;
			}
		};
		s.registerFirst(5098);
	}
}
