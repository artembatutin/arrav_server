package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

import static net.arrav.content.teleport.TeleportType.NORMAL;

public class AbyssalRift extends ActionInitializer {
	@Override
	public void init() {
		final Position[] pos = {new Position(2400, 4850, 0), new Position(2142, 4836, 0), new Position(2466, 4888, 1), new Position(2580, 4843, 0), new Position(2659, 4839, 0), new Position(2522, 4842, 0), new Position(2788, 4839, 0), new Position(2846, 4836, 0), new Position(3486, 4836, 0), new Position(2203, 4836, 0), new Position(2464, 4829, 0), new Position(2271, 4840, 0)};
		final int[] objects = {7133, 7132, 7141, 7129, 7130, 7131, 7140, 7139, 7137, 7136, 7135, 7134};
		//ignored rift
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.message("This altar hasn't been added yet.");
				return true;
			}
		};
		l.registerFirst(7138);
		for(int i = 0; i < objects.length; i++) {
			int ii = i;
			l = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					player.teleport(pos[ii], NORMAL);
					return true;
				}
			};
			l.registerFirst(objects[i]);
		}
	}
}
