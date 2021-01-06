package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.BankPin;
import com.rageps.action.ActionInitializer;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
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
		e.register(60008);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				BankPin.pinButtonAction(player, button);
				return true;
			}
		};
		e.register(60074);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getBank().depositeEquipment();
				return true;
			}
		};
		e.register(60011);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getBank().depositeFamiliar();
				return true;
			}
		};
		e.register(60014);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {

				boolean val = player.getAttributeMap().getBoolean(PlayerAttributes.WITHDRAW_AS_NOTE);
				player.getAttributeMap().set(PlayerAttributes.WITHDRAW_AS_NOTE, !val);
				
				player.send(new ConfigPacket(115, player.getAttributeMap().getBoolean(PlayerAttributes.WITHDRAW_AS_NOTE)? 1 : 0));
				return true;
			}
		};
		e.register(60074);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				//player.getAttr().get("insert_item").set(!(player.getAttr().get("insert_item").getBoolean()));
				player.message("Temporary disabled feature.");
				//player.send(new ConfigPacket(116, player.getAttr().get("insert_item").getBoolean() ? 1 : 0));
				return true;
			}
		};
		e.register(60006);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				int tab = (button - 60032) / 4;
				player.getBank().setTab(tab);
				return true;
			}
		};
		e.register(60032);
		e.register(60036);
		e.register(60040);
		e.register(60044);
		e.register(60048);
		e.register(60052);
		e.register(60056);
		e.register(60060);
		e.register(60064);
		e.register(60068);
	}
	
}
