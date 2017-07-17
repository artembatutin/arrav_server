package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public class SlayerTower extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3432, 3537, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(4493);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3438, 3537, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(4494);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3417, 3540, 2), LADDER);
				return true;
			}
		};
		l.registerFirst(4495);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.teleport(new Position(3412, 3540, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(4496);

	}
}
