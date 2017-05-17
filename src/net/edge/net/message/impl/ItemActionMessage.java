package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.content.Dice;
import net.edge.world.content.combat.magic.lunars.impl.spells.HunterKit;
import net.edge.world.content.item.FoodConsumable;
import net.edge.world.content.item.PotionConsumable;
import net.edge.world.content.minigame.barrows.BarrowsMinigame;
import net.edge.world.content.skill.herblore.Herb;
import net.edge.world.content.skill.prayer.PrayerBoneBury;
import net.edge.world.content.skill.slayer.Slayer;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.world.model.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemDefinition;

/**
 * The message sent from the client when the player clicks an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemActionMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ACTION))
			return;
		
		switch(opcode) {
			case 122:
				firstClick(player, payload);
				break;
			case 75:
				thirdClick(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ACTION);
	}
	
	/**
	 * Handles the first slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void firstClick(Player player, ByteMessage payload) {
		int container = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = payload.getShort(false, ByteTransform.A);
		int id = payload.getShort(false, ByteOrder.LITTLE);
		
		if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
			return;
		
		Item item = player.getInventory().get(slot);
		
		if(item == null || item.getId() != id) {
			return;
		}
		
		player.getCombatBuilder().cooldown(true);
		
		if(container == 3214) {
			if(PrayerBoneBury.produce(player, item)) {
				return;
			}
			if(FoodConsumable.consume(player, item, slot)) {
				return;
			}
			if(PotionConsumable.consume(player, item, slot)) {
				return;
			}
			if(Herb.identify(player, item)) {
				return;
			}
			if(Dice.select(player, item.getId()) || Dice.roll(player, item.getId(), false)) {
				return;
			}
			if(Slayer.contact(player, item, 1)) {
				return;
			}
			
			switch(item.getId()) {
				case 11159:
					if(player.getInventory().remaining() < (HunterKit.ITEMS.length - 2)) {//-2 because we remove the hunter kit toolbox before adding the items
						player.message("You don't have enough inventory space to unlock the hunter kit...");
						return;
					}
					player.getInventory().remove(HunterKit.HUNTER_KIT);
					player.getInventory().addAll(HunterKit.ITEMS);
					break;
				case 10006:
					player.message("disabled for now...");
					//					Hunter.lay(player, new BirdSnare(player));
					break;
				case 10008:
					player.message("disabled for now...");
					//					Hunter.lay(player, new BoxTrap(player));
					break;
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
					
					break;
				case 692:
					player.setRights(Rights.DONATOR);
					player.message("You are now a donator.");
					player.getInventory().remove(new Item(692));
					break;
				case 693:
					player.setRights(Rights.SUPER_DONATOR);
					player.message("You are now a super donator.");
					player.getInventory().remove(new Item(693));
					break;
				case 691:
					player.setRights(Rights.EXTREME_DONATOR);
					player.message("You are now a extreme donator.");
					player.getInventory().remove(new Item(691));
					break;
			}
		}
	}
	
	/**
	 * Handles the third slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	public void thirdClick(Player player, ByteMessage payload) {
		int container = payload.getShort(true, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int id = payload.getShort(true, ByteTransform.A);
		
		if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length) {
			return;
		}
		
		Item item = player.getInventory().get(slot);
		player.getCombatBuilder().cooldown(true);
		
		if(Summoning.summon(player, item, false)) {
			return;
		}
		if(item.getDefinition().getName().contains("Black mask")) {
			player.getInventory().replace(item.getId(), 8921, true);//black mask discharge
		}
	}
}
