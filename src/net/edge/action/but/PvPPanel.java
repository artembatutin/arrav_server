package net.edge.action.but;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.content.teleport.TeleportType;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

public class PvPPanel extends ActionInitializer {
	
	@Override
	public void init() {
		//Bosses 420+
		final Position[] teleports = {new Position(3006, 3628),//fortress
				new Position(3307, 3916),//obelisk 50
				new Position(3241, 3611),//temple
				new Position(2979, 3759),//cemetry
				new Position(2980, 3866),//obelisk 44
				new Position(3171, 3866),//wildywyrm
		};
		for(int i = 0; i < teleports.length; i++) {
			int index = i;
			ButtonAction e = new ButtonAction() {
				@Override
				public boolean click(Player player, int button) {
					player.teleport(teleports[index], TeleportType.NORMAL);
					return true;
				}
			};
			e.register(1164 + i);
		}
	}
	
}
