package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class Brimhaven extends EventInitializer {
	@Override
	public void init() {
		//staires
		ObjectEvent s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(2648, 9592)))
					player.teleport(new Position(2642, 9595, 2), LADDER);
				return true;
			}
		};
		s.registerFirst(5094);
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(2644, 9593, 2)))
					player.teleport(new Position(2649, 9591, 0), LADDER);
				return true;
			}
		};
		s.registerFirst(5096);
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(2635, 9514)))
					player.teleport(new Position(2637, 9510, 2), LADDER);
				return true;
			}
		};
		s.registerFirst(5097);
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(2635, 9511, 2)))
					player.teleport(new Position(2637, 9517, 0), LADDER);
				return true;
			}
		};
		s.registerFirst(5098);
	}
}
