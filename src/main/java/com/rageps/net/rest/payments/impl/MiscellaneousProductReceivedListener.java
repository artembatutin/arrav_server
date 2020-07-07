package com.rageps.net.rest.payments.impl;

import com.rageps.net.rest.payments.Product;
import com.rageps.net.rest.payments.ProductReceivedListener;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Created by Ryley Kimmel on 10/30/2016.
 */
public final class MiscellaneousProductReceivedListener implements ProductReceivedListener {
	public static final int SLAYER_RESET_SCROLL = 19890;

	public static final int CRYSTAL_LUCK_BOX = 19009;

	public static final int VOTING_EXPERT_SCROLL = 19670;

	public static final int DEATHS_BYPASS = 20054;

	public static final int DROP_RATE_INCREASE = 3275;

	public static final int MYSTERY_BOX = 6199;

	public static final int SUPER_MYSTERY_BOX = 6198;

	public static final int TITLE_CHANGE_SCROLL = 2396;

	@Override
	public boolean receive(Player player, Product product) {
		switch (product.getItemName()) {
			case "Rune Pouch":
				return true;
			case "Looting Bag":
				return true;
			case "Dice Bag":
				return true;
			case "Mithril Seeds":
				return true;
			case "Slayer Reset Free Pass":
				add(player, new Item(SLAYER_RESET_SCROLL, product.getQuantity()));
				return true;
			case "Voting Expert":
				add(player, new Item(VOTING_EXPERT_SCROLL, product.getQuantity()));
				return true;
			case "Death's bypass ring":
				add(player, new Item(DEATHS_BYPASS, product.getQuantity()));
				return true;
			case "Drop Rate Increase":
				add(player, new Item(DROP_RATE_INCREASE, product.getQuantity()));
				return true;
			case "Bank command":
				int count = product.getQuantity() * 100;
				//player.addCustomCommand(CustomCommand.BANK, count);
				//player.message(ChatColor.GE_GREEN + "You have received " + count + " uses of the ::bank command!");
				return true;
			case "Restore Command":
				count = product.getQuantity() * 100;
				//player.addCustomCommand(CustomCommand.RESTORE, count);
				//player.message(ChatColor.GE_GREEN + "You have received " + count + " uses of the ::restore command!");
				return true;
			case "Mystery Box":
				add(player, new Item(MYSTERY_BOX, product.getQuantity()));
				return true;
			case "Super Mystery Box":
				add(player, new Item(SUPER_MYSTERY_BOX, product.getQuantity()));
				return true;
			case "Custom Title":
				add(player, new Item(TITLE_CHANGE_SCROLL, product.getQuantity()));
				return true;
			case "Well of Good Will ticket":
				//add(player, new Item(WellOfGoodWillTicket.ITEM_ID, product.getQuantity()));
				return true;
			case "Easter egg":
				//add(player, new Item(Items.EASTER_EGG, product.getQuantity()));
				return true;
			case "Skilling Supply Crate":
				//add(player, new Item(Items.OSRS_SUPPLY_CRATE, product.getQuantity()));
				return true;
		}
		return false;
	}
}
