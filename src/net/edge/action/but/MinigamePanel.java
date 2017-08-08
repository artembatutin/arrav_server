package net.edge.action.but;

import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.player.Player;

public class MinigamePanel extends ActionInitializer {
	
	@Override
	public void init() {
		//Minigames 30-47
		//horroris
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3366, 3512), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(30);
		//barrows
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3565, 3306), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(31);
		//duel arena
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3363, 3275), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(34);
		//pest control
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2658, 2659), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(37);
		//fight pits
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2399, 5178), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(39);
		//warriors guild
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2844, 3542), DefaultTeleportSpell.TeleportType.BOSS_PORTAL);
				return true;
			}
		};
		e.register(40);
		
	}
	
}
