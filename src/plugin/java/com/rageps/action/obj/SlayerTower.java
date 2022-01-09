package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class SlayerTower extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3432, 3537, 1), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(4493);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3438, 3537, 0), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(4494);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3417, 3540, 2), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(4495);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3412, 3540, 1), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(4496);
		
	}
}
