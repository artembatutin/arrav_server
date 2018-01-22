package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.skill.cooking.Cooking;
import net.arrav.content.skill.cooking.CookingData;
import net.arrav.net.packet.out.SendConfig;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.object.GameObject;

public class CookingButton extends ActionInitializer {

	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				CookingData cookingData = (CookingData) player.getAttr().get("cooking_data").get();
				if(cookingData != null) {
					Cooking cooking = new Cooking(player, (GameObject) player.getAttr().get("cooking_object").get(), cookingData, (Boolean) player.getAttr().get("cooking_usingStove").get(), 1);
					cooking.start();
				}
				return true;
			}
		};
		e.register(53152);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				CookingData cookingData1 = (CookingData) player.getAttr().get("cooking_data").get();
				if(cookingData1 != null) {
					Cooking cooking = new Cooking(player, (GameObject) player.getAttr().get("cooking_object").get(), cookingData1, (Boolean) player.getAttr().get("cooking_usingStove").get(), 5);
					cooking.start();
				}
				return true;
			}
		};
		e.register(53151);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				CookingData cookingData2 = (CookingData) player.getAttr().get("cooking_data").get();
				if(cookingData2 != null) {
					int amount = player.getInventory().computeAmountForId(cookingData2.getRawId());
					Cooking cooking = new Cooking(player, (GameObject) player.getAttr().get("cooking_object").get(), cookingData2, (Boolean) player.getAttr().get("cooking_usingStove").get(), amount);
					cooking.start();
				}
				return true;
			}
		};
		e.register(53149);
	}

}
