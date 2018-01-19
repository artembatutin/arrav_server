package net.arrav.action.impl;

import net.arrav.action.Action;
import net.arrav.content.item.*;
import net.arrav.content.object.cannon.Multicannon;
import net.arrav.content.skill.herblore.Herb;
import net.arrav.content.skill.prayer.PrayerBoneBury;
import net.arrav.content.skill.slayer.Slayer;
import net.arrav.content.skill.summoning.SummoningData;
import net.arrav.net.packet.in.ItemActionPacket;
import net.arrav.net.packet.in.ItemInterfacePacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

/**
 * Action handling item action clicks.
 * Can be both interface and action wise.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ItemAction extends Action {
	
	public abstract boolean click(Player player, Item item, int container, int slot, int click);
	
	public void register(int item) {
		ItemActionPacket.ITEM_ACTION.register(item, this);
	}
	
	public void registerEquip(int item) {
		ItemInterfacePacket.EQUIP.register(item, this);
	}
	
	public static void init() {
		PrayerBoneBury.action();
		FoodConsumable.action();
		PotionConsumable.action();
		Herb.action();
		Slayer.actionItem();
		Dice.action();
		MithrilSeed.action();
		SummoningData.action();
		ExperienceLamp.handleItem();
		Multicannon.action();
	}
	
}
