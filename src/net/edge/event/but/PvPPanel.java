package net.edge.event.but;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;

public class PvPPanel extends EventInitializer {
	
	@Override
	public void init() {
		//Bosses 420+
		final Position[] teleports = {
				new Position(3006, 3628),//fortress
				new Position(3307, 3916),//obelisk 50
				new Position(3241, 3611),//temple
				new Position(2979, 3759),//cemetry
				new Position(2980, 3866),//obelisk 44
				new Position(3171, 3866),//wildywyrm
		};
		for(int i = 0; i < teleports.length; i++) {
			int index = i;
			ButtonEvent e = new ButtonEvent() {
				@Override
				public boolean click(Player player, int button) {
					player.teleport(teleports[index], DefaultTeleportSpell.TeleportType.PVP_PORTAL);
					return true;
				}
			};
			e.register(1164 + i);
		}
	}
	
}
