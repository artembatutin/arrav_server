package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.content.PotionDecanting;
import net.edge.world.content.item.ItemCombine;
import net.edge.world.content.skill.cooking.PieCreation;
import net.edge.world.content.skill.cooking.PizzaTopping;
import net.edge.world.content.skill.crafting.*;
import net.edge.world.content.skill.firemaking.Firemaking;
import net.edge.world.content.skill.fletching.*;
import net.edge.world.content.skill.herblore.FinishedPotion;
import net.edge.world.content.skill.herblore.Grinding;
import net.edge.world.content.skill.herblore.TarCreation;
import net.edge.world.content.skill.herblore.UnfinishedPotion;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.cooking.DoughCreation;
import net.edge.world.content.skill.fletching.ArrowCreation.HeadlessArrowCreation;
import net.edge.net.codec.ByteTransform;

/**
 * The message sent from the client when a player uses an item on another item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_ITEM))
			return;
		int secondSlot = payload.getShort();
		int firstSlot = payload.getShort(ByteTransform.A);
		payload.getShort();
		payload.getShort();
		Item itemUsed = player.getInventory().get(firstSlot);
		Item itemOn = player.getInventory().get(secondSlot);
		
		if(secondSlot < 0 || firstSlot < 0 || itemUsed == null || itemOn == null)
			return;
		
		if(PotionDecanting.manual(player, itemUsed, itemOn)) {
			return;
		}
		
		if(Firemaking.execute(player, itemUsed, itemOn)) {
			return;
		}
		if(UnfinishedPotion.produce(player, itemUsed, itemOn)) {
			return;
		}
		if(FinishedPotion.produce(player, itemUsed, itemOn)) {
			return;
		}
		if(PieCreation.create(player, itemUsed, itemOn)) {
			return;
		}
		if(TarCreation.produce(player, itemUsed, itemOn)) {
			return;
		}
		if(HideWorking.openInterface(player, itemUsed, itemOn)) {
			return;
		}
		if(Grinding.produce(player, itemUsed, itemOn)) {
			return;
		}
		if(LeatherWorking.openInterface(player, itemUsed, itemOn)) {
			return;
		}
		if(DoughCreation.openInterface(player, itemUsed, itemOn)) {
			return;
		}
		if(PizzaTopping.add(player, itemUsed, itemOn)) {
			return;
		}
		if(BowCarving.openInterface(player, itemUsed, itemOn, false)) {
			return;
		}
		if(BowStringing.string(player, itemUsed, itemOn)) {
			return;
		}
		if(CrossbowStringing.string(player, itemUsed, itemOn)) {
			return;
		}
		if(CrossbowLimbing.construct(player, itemUsed, itemOn)) {
			return;
		}
		if(ArrowCreation.fletch(player, itemUsed, itemOn)) {
			return;
		}
		if(HeadlessArrowCreation.fletchArrowShaft(player, itemUsed, itemOn)) {
			return;
		}
		if(HeadlessArrowCreation.fletchGrenwallSpikes(player, itemUsed, itemOn)) {
			return;
		}
		if(GemCutting.cut(player, itemUsed, itemOn)) {
			return;
		}
		if(Glassblowing.openInterface(player, itemUsed, itemOn)) {
			return;
		}
		if(AmuletStringing.create(player, itemUsed, itemOn)) {
			return;
		}
		if(BoltCreation.fletch(player, itemUsed, itemOn)) {
			return;
		}
		if(ItemCombine.handle(player, player.getInventory(), itemOn, itemUsed)) {
			return;//using inventory for now, unsure if something else required.
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_ITEM);
	}
}
