package com.rageps.action.impl;

import com.rageps.content.item.ArmourSet;
import com.rageps.content.skill.fletching.DartCreation;
import com.rageps.net.packet.in.ItemOnItemPacket;
import com.rageps.action.Action;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Action handling item on object actions.
 * @author Artem Batutin
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
