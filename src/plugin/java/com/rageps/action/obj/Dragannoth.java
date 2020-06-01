package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.action.ActionInitializer;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.world.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

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
