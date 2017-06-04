package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public class SummoningObelisk extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.message("Summoning will be released soon.");
				return true;
			}
		};
		l.registerFirst(29947);
	}
}
