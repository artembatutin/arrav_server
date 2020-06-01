package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.action.ActionInitializer;
import com.rageps.net.packet.out.SendConfig;
import com.rageps.world.entity.actor.player.Player;

public class CombatSpecialButton extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getCombatSpecial() == null) {
					return true;
				}
				if(!MinigameHandler.execute(player, m -> m.canUseSpecialAttacks(player, player.getCombatSpecial()))) {
					return true;
				}

				if(player.isSpecialActivated()) {
					player.out(new SendConfig(301, 0));
					player.setSpecialActivated(false);
				} else {
					if(player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
						player.message("You do not have enough special energy left!");
						return true;
					}

					player.setSpecialActivated(true);
					player.out(new SendConfig(301, 1));
					player.getCombatSpecial().enable(player);
				}
				return true;
			}
		};
		//SPECIALS
		e.register(29038);
		e.register(29063);
		e.register(29113);
		e.register(29188);
		e.register(29213);
		e.register(48023);
		e.register(7462);
		e.register(7512);
		e.register(12311);
		e.register(7562);
		e.register(7537);
		e.register(7788);
		e.register(7498);
		e.register(8481);
		e.register(7662);
		e.register(7667);
		e.register(7687);
		e.register(7587);
		e.register(7612);
		e.register(7623);
		e.register(7473);
		e.register(12322);
		e.register(29138);
		e.register(29163);
		e.register(29199);
		e.register(29074);
		e.register(33033);
		e.register(29238);
		e.register(30007);
		e.register(30108);
		e.register(48034);
		e.register(29049);
		e.register(30043);
		e.register(29124);
	}
	
}
