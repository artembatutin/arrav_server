package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class HomeLadder extends ActionInitializer {
	@Override
	public void init() {
		//night's watch ladder
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(player.isIronMan())
					player.teleport(new Position(3099, 3497, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(34548);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3102, 3497, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(34550);
		
		//thieving ladder
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3091, 3478, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(24354);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3091, 3480, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(24362);
		
		//home staires
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3084, 3510, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(34499);
		
		//mining ladder
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(2995, 9826, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(24363);
	}
}
