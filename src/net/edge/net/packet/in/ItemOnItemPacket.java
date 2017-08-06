package net.edge.net.packet.in;

import net.edge.action.ActionContainer;
import net.edge.action.impl.ItemOnItemAction;
import net.edge.content.PotionDecanting;
import net.edge.content.item.ItemCombine;
import net.edge.content.skill.cooking.DoughCreation;
import net.edge.content.skill.cooking.PieCreation;
import net.edge.content.skill.cooking.PizzaTopping;
import net.edge.content.skill.crafting.*;
import net.edge.content.skill.firemaking.Firemaking;
import net.edge.content.skill.fletching.*;
import net.edge.content.skill.fletching.ArrowCreation.HeadlessArrowCreation;
import net.edge.content.skill.herblore.FinishedPotion;
import net.edge.content.skill.herblore.Grinding;
import net.edge.content.skill.herblore.TarCreation;
import net.edge.content.skill.herblore.UnfinishedPotion;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;

/**
 * The message sent from the client when a player uses an item on another item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemPacket implements IncomingPacket {
	
	public static final ActionContainer<ItemOnItemAction> ACTIONS = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_ITEM))
			return;
		int secondSlot = payload.getShort();
		int firstSlot = payload.getShort(ByteTransform.A);
		payload.getShort();
		payload.getShort();
		Item itemUsed = player.getInventory().get(firstSlot);
		Item itemOn = player.getInventory().get(secondSlot);
		if(secondSlot < 0 || firstSlot < 0 || itemUsed == null || itemOn == null) {
			return;
		}
		ItemOnItemAction a = ACTIONS.get(itemUsed.getId());
		if(a != null) {
			if(a.click(player, itemOn, itemUsed, firstSlot, secondSlot))
				return;
			else
				a = ACTIONS.get(itemOn.getId());
		}
		if(a == null) {
			a = ACTIONS.get(itemOn.getId());
		}
		if(a != null) {
			if(a.click(player, itemOn, itemUsed, firstSlot, secondSlot))
				return;
		}
		if(PotionDecanting.manual(player, itemUsed, itemOn)) {
			return;
		}
		if(Firemaking.execute(player, itemUsed, itemOn, false)) {
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
