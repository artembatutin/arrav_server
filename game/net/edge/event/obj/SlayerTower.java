package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class SlayerTower extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3432, 3537, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(4493);
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3438, 3537, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(4494);
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3417, 3540, 2), LADDER);
				return true;
			}
		};
		l.registerFirst(4495);
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3412, 3540, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(4496);

	}
}
