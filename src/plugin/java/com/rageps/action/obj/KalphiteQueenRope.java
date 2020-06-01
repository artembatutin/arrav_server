package com.rageps.action.obj;

import com.rageps.GameConstants;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

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
