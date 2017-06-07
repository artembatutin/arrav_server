package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class AgilityStairs extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(2532, 3545, 1)))
						player.teleport(new Position(2532, 3546, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(3205);

	}
}
