package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class HomeLadder extends EventInitializer {
	@Override
	public void init() {
		//night's watch ladder
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3099, 3497, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(34548);
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3102, 3497, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(34550);
		
		//thieving ladder
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3091, 3478, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(24354);
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3091, 3480, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(24362);
		
		//home staires
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3084, 3510, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(34499);
		
		//mining ladder
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(2995, 9826, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(24363);
	}
}
