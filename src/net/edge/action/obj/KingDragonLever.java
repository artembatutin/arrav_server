package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import static net.edge.content.teleport.TeleportType.BOSS_PORTAL;

public class KingDragonLever extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction open = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3220, 3430, 0), BOSS_PORTAL);
				return true;
			}
		};
		open.registerFirst(1817);
	}
}
