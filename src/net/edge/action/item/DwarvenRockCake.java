package net.edge.action.item;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.content.combat.formula.MeleeFormula;
import net.edge.content.skill.Skills;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Hit;
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
				if(player.getCurrentHealth() <= 1)
					return true;

				player.animation(new Animation(829));
				Hit hit = new Hit(1, Hit.HitType.NORMAL, Hit.HitIcon.NONE);
				player.damage(hit);
				return true;
			}
		};
		e.register(7509);
		e.register(7510);
	}
}
