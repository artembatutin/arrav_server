package net.edge.action.but;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.content.teleport.TeleportType;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

public class BossPanel extends ActionInitializer {
	
	@Override
	public void init() {
		//Bosses 50-67
		//Phoenix
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3535, 5186), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(50);
		// Skeletal horror
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3366, 3512), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(51);
		//sea troll queen
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2343, 3687), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(52);
		//Bork
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3114, 5528), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(53);
		//tormented demons
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2601, 5706), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(54);
		//Giant moles
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1752, 5237), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(55);
		//king black dragon
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2272, 4682), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(56);
		//Chaos elemental
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3260, 3923), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(57);
		//kalphite queen
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3508, 9493), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(58);
		//wildywyrm
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3197, 3873), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(59);
		//Nomad
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3360, 5854), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(60);
		//jad
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2439, 5170), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(61);
		//corp
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2968, 4387, 2), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(62);
		//nex
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2903, 5205), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(63);
		//god wars
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2871, 5318, 2), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(64);
		//dagannoth
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1910, 4371), TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(65);
		
	}
	
}
