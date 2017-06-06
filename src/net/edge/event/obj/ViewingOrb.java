package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class ViewingOrb extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.setViewingOrb(new net.edge.content.ViewingOrb(player, new Position(2398, 5150), new Position(2384, 5157), new Position(2409, 5158), new Position(2388, 5138), new Position(2411, 5137)));
				player.getViewingOrb().open();
				return true;
			}
		};
		l.registerFirst(9391);
	}
}
