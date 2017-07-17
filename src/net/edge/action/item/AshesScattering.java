package net.edge.action.item;

import net.edge.content.skill.Skills;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Inventory;

public class AshesScattering extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				player.animation(new Animation(445));
				player.graphic(new Graphic(40));
				player.getInventory().remove(item, slot);
				Skills.experience(player, 62.5, Skills.PRAYER);
				player.message("You scattered the ashes in the wind.");
				return true;
			}
		};
		e.register(20264);
		e.register(20266);
		e.register(20268);
	}
}
