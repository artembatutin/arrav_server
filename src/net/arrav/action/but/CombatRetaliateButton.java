package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.TabInterface;
import net.arrav.content.skill.magic.Spellbook;
import net.arrav.net.packet.out.SendConfig;
import net.arrav.world.entity.actor.combat.magic.CombatSpell;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Equipment;

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
