package net.edge.event.item;

import net.edge.content.dialogue.impl.GiveItemDialogue;
import net.edge.content.skill.hunter.Hunter;
import net.edge.content.skill.hunter.trap.impl.BirdSnare;
import net.edge.content.skill.hunter.trap.impl.BoxTrap;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public class HunterKit extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(player.getInventory().remaining() < (net.edge.content.combat.magic.lunars.impl.spells.HunterKit.ITEMS.length - 2)) {//-2 because we remove the hunter kit toolbox before adding the items
					player.message("You don't have enough inventory space to unlock the hunter kit...");
					return true;
				}
				player.getInventory().remove(net.edge.content.combat.magic.lunars.impl.spells.HunterKit.HUNTER_KIT);
				player.getInventory().addAll(net.edge.content.combat.magic.lunars.impl.spells.HunterKit.ITEMS);
				return true;
			}
		};
		e.registerInventory(11159);
		e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				Hunter.lay(player, new BirdSnare(player));
				return true;
			}
		};
		e.registerInventory(10006);
		e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				Hunter.lay(player, new BoxTrap(player));
				return true;
			}
		};
		e.registerInventory(10008);
	}
}
