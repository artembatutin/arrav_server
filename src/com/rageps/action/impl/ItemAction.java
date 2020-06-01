package com.rageps.action.impl;

import com.rageps.content.item.*;
import com.rageps.content.object.cannon.Multicannon;
import com.rageps.content.skill.herblore.Herb;
import com.rageps.content.skill.prayer.PrayerBoneBury;
import com.rageps.content.skill.slayer.Slayer;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.net.packet.in.ItemActionPacket;
import com.rageps.net.packet.in.ItemInterfacePacket;
import com.rageps.action.Action;
import com.rageps.content.item.*;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Action handling item action clicks.
 * Can be both interface and action wise.
 * @author Artem Batutin
 */
public abstract class ItemAction extends Action {
	
	public abstract boolean click(Player player, Item item, int container, int slot, int click);
	
	public void register(int item) {
		ItemActionPacket.ITEM_ACTION.register(item, this);
	}

	public void register(int... items) {
		for(int item : items) {
			register(item);
		}
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
