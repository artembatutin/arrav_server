package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

import static net.arrav.content.teleport.TeleportType.NORMAL;

public class KingDragonLever extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction open = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3220, 3430, 0), NORMAL);
				return true;
			}
		};
		open.registerFirst(1817);
	}
}
