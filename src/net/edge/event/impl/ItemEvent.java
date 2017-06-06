package net.edge.event.impl;

import net.edge.content.Dice;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.skill.herblore.Herb;
import net.edge.content.skill.prayer.PrayerBoneBury;
import net.edge.content.skill.slayer.Slayer;
import net.edge.event.Event;
import net.edge.net.packet.impl.ItemActionPacket;
import net.edge.net.packet.impl.ItemInterfacePacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

/**
 * Event handling item action clicks.
 * Can be both interface and action wise.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ItemEvent extends Event {
	
	public abstract boolean click(Player player, Item item, int container, int slot, int click);
	
	public void registerInventory(int item) {
		ItemActionPacket.INVENTORY.register(item, this);
	}
	
	public void registerInterface(int item) {
		ItemInterfacePacket.EQUIP.register(item, this);
	}
	
	public static void init() {
		PrayerBoneBury.event();
		FoodConsumable.event();
		PotionConsumable.event();
		Herb.event();
		Slayer.eventItem();
		Dice.event();
	}
	
}
