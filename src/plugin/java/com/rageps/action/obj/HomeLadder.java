package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class HomeLadder extends ActionInitializer {
	@Override
	public void init() {
		//iron man ladder
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(player.isIronMan())
					player.teleport(new Position(3099, 3497, 1), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(34548);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3102, 3497, 0), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(34550);
		
		//thieving ladder
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3091, 3478, 1), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(24354);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3091, 3480, 0), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(24362);
		
		//home staires
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3081, 3504, 0), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(34499);
		
		//mining ladder
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(2995, 9826, 0), TeleportType.LADDER);
				return true;
			}
		};
		l.registerFirst(24363);
	}
}
