package net.edge.event.item;

import net.edge.content.dialogue.impl.GiveItemDialogue;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;

import java.util.Optional;

public class NightsPackage extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				player.getInventory().remove(item);
				player.getDialogueBuilder().append(new GiveItemDialogue(new Item(18741), "Nightmare cape from behalf of the Night's watch.", Optional.empty()));
				return true;
			}
		};
		e.registerInventory(15246);
	}
}
