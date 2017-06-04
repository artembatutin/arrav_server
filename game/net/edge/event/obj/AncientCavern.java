package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class AncientCavern extends EventInitializer {
	@Override
	public void init() {
		//staires
		ObjectEvent s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(1778, 5344, 0)))
					player.move(new Position(1778, 5343, 1));
				return true;
			}
		};
		s.registerFirst(67342);
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(1778, 5344, 1)))
					player.move(new Position(1778, 5346, 0));
				return true;
			}
		};
		s.registerFirst(67343);
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(1744, 5322, 1)))
					player.move(new Position(1745, 5325, 0));
				return true;
			}
		};
		s.registerFirst(81471);
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(1744, 5323, 0)))
					player.move(new Position(1744, 5321, 1));
				return true;
			}
		};
		s.registerFirst(67340);
	}
}
