package net.edge.action.impl;

import net.edge.action.Action;
import net.edge.content.skill.cooking.Cooking;
import net.edge.content.skill.crafting.JewelleryMoulding;
import net.edge.content.skill.crafting.Spinning;
import net.edge.content.skill.farming.FarmingAction;
import net.edge.content.skill.firemaking.Bonfire;
import net.edge.content.skill.prayer.PrayerBoneAltar;
import net.edge.net.packet.in.ItemOnItemPacket;
import net.edge.net.packet.in.ItemOnObjectPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

/**
 * Action handling item on object actions.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ItemOnItemAction extends Action {
	
	public abstract boolean click(Player player, Item itemOn, Item itemUsed, int slotFirst, int slotSecond);
	
	public void register(int item) {
		ItemOnItemPacket.ACTIONS.register(item, this);
	}
	
	public static void init() {

	}
	
}
