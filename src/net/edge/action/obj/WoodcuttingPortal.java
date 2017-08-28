package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.GameObject;

public class WoodcuttingPortal extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction open = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				//				player.teleport(new Position(3220, 3433, 0), TRAINING_PORTAL); TODO: add teleports
				return true;
			}
		};
		open.registerFirst(8972);
	}
}
