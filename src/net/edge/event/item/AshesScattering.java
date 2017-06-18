package net.edge.event.item;

import net.edge.content.skill.Skills;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.impl.Inventory;

public class AshesScattering extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
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
