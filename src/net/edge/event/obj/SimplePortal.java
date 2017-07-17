package net.edge.event.obj;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.game.GameConstants;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class SimplePortal extends EventInitializer {
	@Override
	public void init() {
		//Green portal at home
		ObjectEvent portal = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(object.getGlobalPos().same(new Position(3005, 3963, 0)))//wilderness agility
						player.teleport(GameConstants.STARTING_POSITION, DefaultTeleportSpell.TeleportType.OBELISK);
				if(object.getGlobalPos().same(new Position(2996, 9823, 0)))//to rune essence
						player.teleport(new Position(2922, 4819, 0), DefaultTeleportSpell.TeleportType.FREEZE);
				if(object.getGlobalPos().same(new Position(2922, 4819, 0)))//from rune essence
						player.teleport(new Position(2996, 9823, 0), DefaultTeleportSpell.TeleportType.FREEZE);
				return true;
			}
		};
		portal.registerFirst(2273);
		
	}
}
