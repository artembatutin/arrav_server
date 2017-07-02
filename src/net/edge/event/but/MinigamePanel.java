package net.edge.event.but;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;

public class MinigamePanel extends EventInitializer {
	
	@Override
	public void init() {
		//Minigames 30-47
		//barrows
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3565, 3306), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(31);
		//duel arena
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3363, 3275), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(34);
		//pest control
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2658, 2659), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(37);
		//fight pits
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2399, 5178), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(39);
		//warriors guild
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2844, 3542), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(40);
		
	}
	
}
