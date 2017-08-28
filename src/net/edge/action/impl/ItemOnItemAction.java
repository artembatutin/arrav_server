package net.edge.action.impl;

import net.edge.action.Action;
import net.edge.content.item.ArmourSet;
import net.edge.content.skill.fletching.DartCreation;
import net.edge.net.packet.in.ItemOnItemPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

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
