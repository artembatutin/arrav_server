package net.edge.event.but;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;

public class MonsterPanel extends EventInitializer {
	
	@Override
	public void init() {
		//Monsters 70-100
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2681, 3728), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(70);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2884, 9798), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(71);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2713, 9564), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(72);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3097, 9876), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(73);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2896, 2724), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(74);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3429, 3538), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(75);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2806, 10002, 0), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(76);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1751, 5290, 1), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(77);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1488, 4704, 0), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(78);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(4170, 5706, 0), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(79);

	}
	
}
