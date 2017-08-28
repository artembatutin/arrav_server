package net.edge.action.item;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.hit.Hitsplat;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Inventory;

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
				Hit hit = new Hit(10, Hitsplat.NORMAL, HitIcon.NONE);
				player.damage(hit);
				return true;
			}
		};
		e.register(7509);
		e.register(7510);
	}
}
