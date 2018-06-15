package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.BankPin;
import net.arrav.net.packet.out.SendConfig;
import net.arrav.world.entity.actor.player.Player;

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
				player.getAttr().get("withdraw_as_note").set(!(player.getAttr().get("withdraw_as_note").getBoolean()));
				player.out(new SendConfig(115, player.getAttr().get("withdraw_as_note").getBoolean() ? 1 : 0));
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
