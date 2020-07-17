package com.rageps.net.packet.in;

import com.rageps.RagePS;
import com.rageps.action.ActionContainer;
import com.rageps.action.impl.ButtonAction;
import com.rageps.content.TabInterface;
import com.rageps.content.dialogue.Dialogues;
import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.cooking.DoughCreation;
import com.rageps.content.skill.crafting.*;
import com.rageps.content.skill.fletching.BowCarving;
import com.rageps.content.skill.magic.EnchantCrossbowBolts;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.content.skill.slayer.Slayer;
import com.rageps.content.skill.smithing.Smelting;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.magic.lunars.LunarSpells;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;

/**
 * The message sent from the client when the player clicks some sort of button or
 * module.
 * @author lare96 <http://github.com/lare96>
 */
public final class ClickButtonPacket implements IncomingPacket {
	
	/**
	 * Buttons {@link ActionContainer} instance.
	 */
	public static final ActionContainer<ButtonAction> BUTTONS = new ActionContainer<>();
	
	/**
	 * The flag that determines if this message should be read properly.
	 */
	private static final boolean PROPER_READ = false;
	
	private static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for(byte aData : data) {
			int num = (aData & 0xFF) * n;
			value += num;
			if(n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		int button = PROPER_READ ? buf.getShort() : hexToInt(buf.getBytes(2));
		if(World.get().getEnvironment().isDebug() && player.getRights().equals(Rights.ADMINISTRATOR)) {
			player.message("Clicked button " + button + ".");
		}
		
		if(button != 9154 && button != 200 && button != 201 && player.getActivityManager().contains(ActivityManager.ActivityType.CLICK_BUTTON)) {
			return;
		}
		if(button == 123) {
			player.closeWidget();
			if(player.getMarketShop() != null) {
				MarketShop.clearFromShop(player);
			}
		}
		ButtonAction e = BUTTONS.get(button);
		if(e != null) {
			if(e.click(player, button)) {
				return;
			}
		}
		if(Prayer.activate(player, true, button)) {
			return;
		}
		if(Prayer.activateQuickPrayer(player, button)) {
			return;
		}
		if(Prayer.toggleQuickPrayer(player, button)) {
			return;
		}
		if(EnchantCrossbowBolts.openInterface(player, button) || EnchantCrossbowBolts.enchant(player, button)) {
			return;
		}
		if(BowCarving.fletch(player, button)) {
			return;
		}
		if(Summoning.withdrawAll(player, button)) {
			return;
		}
		if(Smelting.smelt(player, button)) {
			return;
		}
		if(Glassblowing.blow(player, button)) {
			return;
		}
		if(Tanning.create(player, button)) {
			return;
		}
		if(DoughCreation.create(player, button)) {
			return;
		}
		if(Spinning.create(player, button)) {
			return;
		}
		if(PotClaying.create(player, button)) {
			return;
		}
		if(HideWorking.create(player, button)) {
			return;
		}
		if(LeatherWorking.create(player, button)) {
			return;
		}
		if(ExchangeSessionManager.get().buttonClickAction(player, button)) {
			return;
		}
		if(Dialogues.executeOptionListeners(player, button)) {
			return;
		}
		if(SkillData.sendEnterGoalLevel(player, button)) {
			return;
		}
		if(Slayer.clickButton(player, button)) {
			return;
		}
		if(LunarSpells.castButtonSpell(player, button)) {
			return;
		}
		switch(button) {
			case 55095:
				Item item = player.getInventory().get(player.getAttributeMap().getInt(PlayerAttributes.DESTROY_ITEM_SLOT));
				player.getInventory().remove(item);
				player.getAttributeMap().set(PlayerAttributes.DESTROY_ITEM_SLOT,-1);
				player.closeWidget();
				break;
			case 55096:
				player.closeWidget();
				break;
			/*case 195212:
				if(player.getClan().isPresent())
					if(player.getClan().get().getRank().getValue() >= player.getClan().get().getClan().getLowest().getValue())
						player.widget(-3);
					else
						player.getClan().get().sendMessage("You don't have the requirements to do that.");
				else
					player.out(new SendEnterName("Your clan chat name:", t -> () -> ClanManager.get().create(player, t)));
				break;*/
			case 21341://equipment screen
				player.widget(15106);
				break;
			/*case 195209: todo reimplement
				if(player.getClan().isPresent())
					ClanManager.get().exit(player);
				else
					player.out(new SendEnterName("Enter the name of the chat you wish to join.", s -> () -> ClanManager.get().join(player, s)));
				break;*/
			case 24017:
			case 7212:
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				break;
			
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.CLICK_BUTTON);
	}
}
