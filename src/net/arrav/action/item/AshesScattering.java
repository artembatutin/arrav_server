package net.arrav.action.item;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ItemAction;
import net.arrav.content.skill.Skills;
import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Inventory;

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
