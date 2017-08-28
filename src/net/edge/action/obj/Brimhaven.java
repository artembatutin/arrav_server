package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

public class Brimhaven extends ActionInitializer {
	@Override
	public void init() {
		//staires
		ObjectAction s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(2648, 9592)))
					;
				//				player.teleport(new Position(2642, 9595, 2), LADDER); TODO: add teleports
				return true;
			}
		};
		s.registerFirst(5094);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(2644, 9593, 2)))
					;
				//				player.teleport(new Position(2649, 9591, 0), LADDER);
				return true;
			}
		};
		s.registerFirst(5096);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(2635, 9514)))
					;
				//				player.teleport(new Position(2637, 9510, 2), LADDER);
				return true;
			}
		};
		s.registerFirst(5097);
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(2635, 9511, 2)))
					;
				//				player.teleport(new Position(2637, 9517, 0), LADDER);
				return true;
			}
		};
		s.registerFirst(5098);
	}
}
