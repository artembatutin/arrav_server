package net.edge.content.item;

import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.content.Dice;
import net.edge.content.combat.magic.lunars.impl.spells.HunterKit;
import net.edge.content.container.impl.Inventory;
import net.edge.content.dialogue.impl.GiveItemDialogue;
import net.edge.content.minigame.barrows.BarrowsMinigame;
import net.edge.content.skill.herblore.Herb;
import net.edge.content.skill.prayer.PrayerBoneBury;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.summoning.Summoning;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;

import java.util.Optional;

public class ItemAction {
	
	public static boolean first(Player player, Item item, int container, int slot) {
		if(container == Inventory.INVENTORY_DISPLAY_ID) {
			if(PrayerBoneBury.produce(player, item))
				return true;
			if(FoodConsumable.consume(player, item, slot))
				return true;
			if(PotionConsumable.consume(player, item, slot))
				return true;
			if(Herb.identify(player, item))
				return true;
			if(Dice.select(player, item.getId()) || Dice.roll(player, item.getId(), false))
				return true;
			if(Slayer.contact(player, item, 1))
				return true;
			switch(item.getId()) {
				case 15246:
					player.getInventory().remove(item);
					player.getDialogueBuilder().append(new GiveItemDialogue(new Item(18741), "Nightmare cape from behalf of the Night's watch.", Optional.empty()));
					return true;
				case 692:
					player.setRights(Rights.DONATOR);
					player.message("You are now a donator.");
					player.getInventory().remove(new Item(692));
					return true;
				case 693:
					player.setRights(Rights.SUPER_DONATOR);
					player.message("You are now a super donator.");
					player.getInventory().remove(new Item(693));
					return true;
				case 691:
					player.setRights(Rights.EXTREME_DONATOR);
					player.message("You are now a extreme donator.");
					player.getInventory().remove(new Item(691));
					return true;
				case 11159:
					if(player.getInventory().remaining() < (HunterKit.ITEMS.length - 2)) {//-2 because we remove the hunter kit toolbox before adding the items
						player.message("You don't have enough inventory space to unlock the hunter kit...");
						return true;
					}
					player.getInventory().remove(HunterKit.HUNTER_KIT);
					player.getInventory().addAll(HunterKit.ITEMS);
					return true;
				case 10006:
					player.message("disabled for now...");
					//Hunter.lay(player, new BirdSnare(player));
					return true;
				case 10008:
					player.message("disabled for now...");
					//Hunter.lay(player, new BoxTrap(player));
					return true;
				case 952:
					LinkedTaskSequence seq = new LinkedTaskSequence();
					seq.connect(1, () -> player.animation(new Animation(830)));
					seq.connect(3, () -> {
						if(BarrowsMinigame.dig(player)) {
							return;
						}
						player.message("You found nothing interesting...");
					});
					seq.start();
					return true;
			}
		}
		return false;
	}
	
	public static boolean third(Player player, Item item, int container, int slot) {
		if(Summoning.summon(player, item, false)) {
			return true;
		}
		if(item.getDefinition().getName().contains("Black mask")) {
			player.getInventory().replace(item.getId(), 8921, true);//black mask discharge
		}
		return false;
	}
}
