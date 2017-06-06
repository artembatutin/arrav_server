package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.TRAINING_PORTAL;

public class ChaosDwarf extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(1487, 4704)))
						player.teleport(new Position(3085, 3513, 0), TRAINING_PORTAL);
				return true;
			}
		};
		l.registerFirst(87012);
	}
}
