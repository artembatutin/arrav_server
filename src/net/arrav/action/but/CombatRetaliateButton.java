package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.world.entity.actor.player.Player;

public class CombatRetaliateButton extends ActionInitializer {

	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(!player.isAutoRetaliate()) {
					player.setAutoRetaliate(true);
					player.message("Auto retaliate has been turned on!");
				} else {
					player.setAutoRetaliate(false);
					player.message("Auto retaliate has been turned off!");
				}
				return true;
			}
		};
		e.register(89061);
		e.register(93202);
	}

}
