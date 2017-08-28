package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

public class SimplePortal extends ActionInitializer {
	@Override
	public void init() {
		//Green portal at home
		ObjectAction portal = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getGlobalPos().same(new Position(3005, 3963, 0))) ;//wilderness agility
//						player.teleport(GameConstants.STARTING_POSITION, DefaultTeleportSpell.TeleportType.OBELISK); TODO: add teleports
				if(object.getGlobalPos().same(new Position(2996, 9823, 0))) ;//to rune essence
//						player.teleport(new Position(2922, 4819, 0), DefaultTeleportSpell.TeleportType.FREEZE);
				if(object.getGlobalPos().same(new Position(2922, 4819, 0))) ;//from rune essence
//						player.teleport(new Position(2996, 9823, 0), DefaultTeleportSpell.TeleportType.FREEZE);
				return true;
			}
		};
		portal.registerFirst(2273);
		portal.registerFirst(44276);

	}
}
