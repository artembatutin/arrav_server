package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.GameObject;

public class AncientCavern extends ActionInitializer {
	@Override
	public void init() {
		//staires
		ObjectAction s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(1778, 5344, 0)))
					player.move(new Position(1778, 5343, 1));
				return true;
			}
		};
		s.registerFirst(67342);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(1778, 5344, 1)))
					player.move(new Position(1778, 5346, 0));
				return true;
			}
		};
		s.registerFirst(67343);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(1744, 5322, 1)))
					player.move(new Position(1745, 5325, 0));
				return true;
			}
		};
		s.registerFirst(81471);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(1744, 5323, 0)))
					player.move(new Position(1744, 5321, 1));
				return true;
			}
		};
		s.registerFirst(67340);
	}
}
