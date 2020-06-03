package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.BankPin;
import com.rageps.action.ActionInitializer;
import com.rageps.net.packet.out.SendConfig;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;

public class BankingButton extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getBank().depositeInventory();
				return true;
			}
		};
		e.register(82018);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				BankPin.pinButtonAction(player, button);
				return true;
			}
		};
		e.register(58025);
		e.register(58026);
		e.register(58027);
		e.register(58028);
		e.register(58029);
		e.register(58030);
		e.register(58031);
		e.register(58032);
		e.register(58033);
		e.register(58034);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getBank().depositeEquipment();
				return true;
			}
		};
		e.register(231047);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getBank().depositeFamiliar();
				return true;
			}
		};
		e.register(231043);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {

				boolean val = player.getAttributeMap().getBoolean(PlayerAttributes.WITHDRAW_AS_NOTE);
				player.getAttributeMap().set(PlayerAttributes.WITHDRAW_AS_NOTE, !val);
				player.out(new SendConfig(115, player.getAttributeMap().getBoolean(PlayerAttributes.WITHDRAW_AS_NOTE)? 1 : 0));
				return true;
			}
		};
		e.register(231041);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				//player.getAttr().get("insert_item").set(!(player.getAttr().get("insert_item").getBoolean()));
				player.message("Temporary disabled feature.");
				//player.out(new SendConfig(116, player.getAttr().get("insert_item").getBoolean() ? 1 : 0));
				return true;
			}
		};
		e.register(231037);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getBank().setTab(button - 100);
				return true;
			}
		};
		for(int i = 100; i <= 109; i++)
			e.register(i);
	}
	
}
