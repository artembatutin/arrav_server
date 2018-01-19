package net.arrav.action.item;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ItemAction;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.hit.HitIcon;
import net.arrav.world.entity.actor.combat.hit.Hitsplat;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Inventory;

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
