package net.edge.event.but;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;

public class BossPanel extends EventInitializer {
	
	@Override
	public void init() {
		//Bosses 50-67
		//sea troll queen
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2601, 5706), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(54);
		//tormented demons
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2601, 5706), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(54);
		//king black dragon
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2272, 4682), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(56);
		//kalphite queen
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3508, 9493), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(58);
		//wildywyrm
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3197, 3873), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(59);
		//jad
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2439, 5170), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(61);
		//corp
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2968, 4387, 2), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(62);
		//god wars
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2871, 5318, 2), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(64);
		//dagannoth
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(1910, 4371), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(65);

	}
	
}
