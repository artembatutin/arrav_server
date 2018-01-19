package net.arrav.action.impl;

import net.arrav.action.Action;
import net.arrav.content.item.ArmourSet;
import net.arrav.content.skill.fletching.DartCreation;
import net.arrav.net.packet.in.ItemOnItemPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

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
		ArmourSet.action();
		DartCreation.action();
	}
	
}
