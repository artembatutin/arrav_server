package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class CorporealBeast extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(player.getPosition().getX() == 2970) {//entering
					player.move(new Position(2974, player.getPosition().getY(), 2));
				} else {
					player.move(new Position(2970, player.getPosition().getY(), 2));
				}
				return true;
			}
		};
		l.registerFirst(38811);
	}
}
