package com.rageps.action.item;

import com.rageps.action.ActionInitializer;
import com.rageps.action.impl.ItemAction;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

public class DwarvenRockCake extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				if(player.getCurrentHealth() <= 10)
					return true;
				player.animation(new Animation(829));
				Hit hit = new Hit(10, Hitsplat.NORMAL_LOCAL, HitIcon.NONE);
				player.damage(hit);
				return true;
			}
		};
		e.register(7509);
		e.register(7510);
	}
}
