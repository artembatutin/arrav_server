package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.task.LinkedTaskSequence;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

public class Dragannoth extends ActionInitializer {
	@Override
	public void init() {
		//down
		ObjectAction s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.move(new Position(1910, 4367));
				return true;
			}
		};
		s.registerFirst(52232);
		//up
		s = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.animation(new Animation(827));
				LinkedTaskSequence seq2 = new LinkedTaskSequence();
				seq2.connect(2, () -> player.move(new Position(2899, 4449)));
				seq2.start();
				return true;
			}
		};
		s.registerFirst(10230);
	}
}
