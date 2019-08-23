package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.locale.Position;

import static net.arrav.content.teleport.TeleportType.LADDER;

public class SlayerTower extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3432, 3537, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(4493);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3438, 3537, 0), LADDER);
				return true;
			}
		};
		l.registerFirst(4494);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3417, 3540, 2), LADDER);
				return true;
			}
		};
		l.registerFirst(4495);
		l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3412, 3540, 1), LADDER);
				return true;
			}
		};
		l.registerFirst(4496);
		
	}
}
