package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public class Dragannoth extends EventInitializer {
	@Override
	public void init() {
		//down
		ObjectEvent s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.move(new Position(1910, 4367));
				return true;
			}
		};
		s.registerFirst(52232);
		//up
		s = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
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
