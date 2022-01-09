package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.skill.cooking.Cooking;
import com.rageps.content.skill.cooking.CookingData;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.object.GameObject;

public class CookingButton extends ActionInitializer {

	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				CookingData cookingData = player.getAttributeMap().getObject(PlayerAttributes.COOKING_DATA);
				if(cookingData != null) {
					Cooking cooking = new Cooking(player, player.getAttributeMap().getObject(PlayerAttributes.COOKING_OBJECT), cookingData, player.getAttributeMap().getBoolean(PlayerAttributes.COOKING_USINGSTOVE), 1);
					cooking.start();
				}
				return true;
			}
		};
		e.register(53152);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				CookingData cookingData1 = player.getAttributeMap().getObject(PlayerAttributes.COOKING_DATA);
				if(cookingData1 != null) {
					Cooking cooking = new Cooking(player, player.getAttributeMap().getObject(PlayerAttributes.COOKING_OBJECT), cookingData1, player.getAttributeMap().getBoolean(PlayerAttributes.COOKING_USINGSTOVE), 5);
					cooking.start();
				}
				return true;
			}
		};
		e.register(53151);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				CookingData cookingData2 = player.getAttributeMap().getObject(PlayerAttributes.COOKING_DATA);
				if(cookingData2 != null) {
					int amount = player.getInventory().computeAmountForId(cookingData2.getRawId());
					Cooking cooking = new Cooking(player, player.getAttributeMap().getObject(PlayerAttributes.COOKING_OBJECT), cookingData2, player.getAttributeMap().getBoolean(PlayerAttributes.COOKING_USINGSTOVE), amount);
					cooking.start();
				}
				return true;
			}
		};
		e.register(53149);
	}

}
