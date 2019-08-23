package net.arrav.action.obj;

import net.arrav.GameConstants;
import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.content.teleport.TeleportType;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.locale.Position;

public class KalphiteQueenRope extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3507, 9494), TeleportType.LADDER);
				return true;
			}
		};
		a.registerFirst(65613);
		a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3509, 9496, 2), TeleportType.LADDER);
				return true;
			}
		};
		a.registerFirst(45835);
		a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(GameConstants.STARTING_POSITION, TeleportType.LADDER);
				return true;
			}
		};
		a.registerFirst(45832);
	}
}
