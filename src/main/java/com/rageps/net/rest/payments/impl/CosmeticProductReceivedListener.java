package com.rageps.net.rest.payments.impl;

import com.rageps.net.rest.payments.Product;
import com.rageps.net.rest.payments.ProductReceivedListener;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Created by Ryley Kimmel on 11/20/2016.
 */
public final class CosmeticProductReceivedListener implements ProductReceivedListener {
	@Override
	public boolean receive(Player player, Product product) {
		switch (product.getItemName()) {
			case "Red Partyhat":
				add(player, new Item(1038, product.getQuantity()));
				return true;
			case "Discord Doug":
				add(player, new Item(1038, product.getQuantity()));
				return true;
			case "White Partyhat":
				add(player, new Item(1048, product.getQuantity()));
				return true;
			case "Purple Partyhat":
				add(player, new Item(1046, product.getQuantity()));
				return true;
			case "Blue Partyhat":
				add(player, new Item(1042, product.getQuantity()));
				return true;
			case "Yellow Partyhat":
				add(player, new Item(1040, product.getQuantity()));
				return true;
			case "Green Partyhat":
				add(player, new Item(1044, product.getQuantity()));
				return true;
			case "Black Partyhat":
				add(player, new Item(20073, product.getQuantity()));
				return true;
			case "Santa Hat":
				add(player, new Item(1050, product.getQuantity()));
				return true;
			case "Black Santa Hat":
				add(player, new Item(10284, product.getQuantity()));
				return true;
			case "Scythe":
				add(player, new Item(1419, product.getQuantity()));
				return true;
			case "Blue H'ween Mask":
				add(player, new Item(1055, product.getQuantity()));
				return true;
			case "Red H'ween Mask":
				add(player, new Item(1057, product.getQuantity()));
				return true;
			case "Green H'ween Mask":
				add(player, new Item(1053, product.getQuantity()));
				return true;
			case "Black H'ween Mask":
				add(player, new Item(20085, product.getQuantity()));
				return true;
			case "Grain":
				add(player, new Item(5607, product.getQuantity()));
				return true;
			case "Sled":
				add(player, new Item(4084, product.getQuantity()));
				return true;
			case "Skeleton Set":
				add(player, new Item(9921, product.getQuantity()));
				add(player, new Item(9922, product.getQuantity()));
				add(player, new Item(9923, product.getQuantity()));
				add(player, new Item(9924, product.getQuantity()));
				add(player, new Item(9925, product.getQuantity()));
				return true;
			case "Santa Outfit":
				add(player, new Item(42887, product.getQuantity()));
				add(player, new Item(42888, product.getQuantity()));
				add(player, new Item(42889, product.getQuantity()));
				add(player, new Item(42890, product.getQuantity()));
				add(player, new Item(42891, product.getQuantity()));
				return true;
			case "Basket of eggs":
				return true;
			case "Easter carrot":
				return true;
			case "Easter ring":
				return true;
			case "Easter egg helm":
				return true;
			case "Rubber chicken":
				return true;
			case "Bunny ears":
				return true;
		}
		return false;
	}
}
