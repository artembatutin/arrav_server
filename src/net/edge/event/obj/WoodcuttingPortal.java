package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.TRAINING_PORTAL;

public class WoodcuttingPortal extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent open = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3220, 3433, 0), TRAINING_PORTAL);
				return true;
			}
		};
		open.registerFirst(8972);
	}
}
