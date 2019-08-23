package net.arrav.net.packet.in;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.action.ActionContainer;
import net.arrav.action.impl.ItemOnItemAction;
import net.arrav.content.item.ItemCombine;
import net.arrav.content.item.PotionDecanting;
import net.arrav.content.skill.cooking.DoughCreation;
import net.arrav.content.skill.cooking.PieCreation;
import net.arrav.content.skill.cooking.PizzaTopping;
import net.arrav.content.skill.crafting.*;
import net.arrav.content.skill.firemaking.Firemaking;
import net.arrav.content.skill.fletching.*;
import net.arrav.content.skill.fletching.ArrowCreation.HeadlessArrowCreation;
import net.arrav.content.skill.herblore.FinishedPotion;
import net.arrav.content.skill.herblore.Grinding;
import net.arrav.content.skill.herblore.TarCreation;
import net.arrav.content.skill.herblore.UnfinishedPotion;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.Item;

/**
 * The message sent from the client when a player uses an item on another item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemPacket implements IncomingPacket {
	
	public static final ActionContainer<ItemOnItemAction> ACTIONS = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_ITEM))
			return;
		int secondSlot = buf.getShort();
		int firstSlot = buf.getShort(ByteTransform.A);
		buf.getShort();
		buf.getShort();
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
