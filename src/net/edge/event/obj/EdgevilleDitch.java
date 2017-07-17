package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.Animation;
import net.edge.world.node.actor.move.ForcedMovement;
import net.edge.world.node.actor.move.ForcedMovementManager;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class EdgevilleDitch extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent jump = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				ForcedMovement jump;
				int wild = 3;
				if(player.getPosition().getX() < 3000 && player.getPosition().getX() > 2994) {
					if(player.getPosition().getX() > 2997)
						wild = -3;
					jump = ForcedMovement.create(player, player.getPosition().move(wild, 0), new Animation(6132));
				} else {
					if(player.getPosition().getY() > 3520)
						wild = -3;
					jump = ForcedMovement.create(player, player.getPosition().move(0, wild), new Animation(6132));
				}
				jump.setSecondSpeed(50);
				ForcedMovementManager.submit(player, jump);
				return true;
			}
		};
		jump.registerFirst(23271);
	}
}
