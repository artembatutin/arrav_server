package net.edge.action.but;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.player.Player;

public class BossPanel extends ActionInitializer {
	
	@Override
	public void init() {
		//Bosses 50-67
		//sea troll queen
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2601, 5706), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(52);
		//Bork
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3114, 5528), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(53);
		//tormented demons
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2601, 5706), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(54);
		//king black dragon
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2272, 4682), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(56);
		//kalphite queen
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3508, 9493), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(58);
		//wildywyrm
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3197, 3873), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(59);
		//Nomad
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3360, 5854), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(60);
		//jad
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2439, 5170), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(61);
		//corp
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2968, 4387, 2), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(62);
		//god wars
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2871, 5318, 2), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(64);
		//dagannoth
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1910, 4371), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(65);

	}
	
}
