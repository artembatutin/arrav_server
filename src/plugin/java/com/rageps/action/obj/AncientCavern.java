package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class AncientCavern extends ActionInitializer {
	@Override
	public void init() {
		//staires
		ObjectAction s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(1778, 5344, 0)))
					player.move(new Position(1778, 5343, 1));
				return true;
			}
		};
		s.registerFirst(67342);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(1778, 5344, 1)))
					player.move(new Position(1778, 5346, 0));
				return true;
			}
		};
		s.registerFirst(67343);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(1744, 5322, 1)))
					player.move(new Position(1745, 5325, 0));
				return true;
			}
		};
		s.registerFirst(81471);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(1744, 5323, 0)))
					player.move(new Position(1744, 5321, 1));
				return true;
			}
		};
		s.registerFirst(67340);
	}
}
