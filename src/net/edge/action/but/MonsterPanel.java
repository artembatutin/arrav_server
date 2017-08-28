package net.edge.action.but;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.content.teleport.TeleportType;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

public class MonsterPanel extends ActionInitializer {
	
	@Override
	public void init() {
		//Monsters 70-100
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2681, 3728), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(70);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2884, 9798), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(71);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2713, 9564), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(72);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3097, 9876), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(73);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2896, 2724), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(74);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3429, 3538), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(75);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2806, 10002, 0), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(76);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1751, 5290, 1), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(77);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1488, 4704, 0), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(78);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(4170, 5706, 0), TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(79);
		
	}
	
}
