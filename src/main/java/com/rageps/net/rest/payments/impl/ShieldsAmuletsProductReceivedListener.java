package com.rageps.net.rest.payments.impl;

import com.rageps.net.rest.payments.Product;
import com.rageps.net.rest.payments.ProductReceivedListener;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Created by sALOON on 8/25/2017.
 */
public final class ShieldsAmuletsProductReceivedListener implements ProductReceivedListener {
	@Override
	public boolean receive(Player player, Product product) {
		switch (product.getItemName()) {
			case "Arcane Spirit Shield":
				add(player, new Item(13738, product.getQuantity()));
				return true;
			case "Spectral Spirit Shield":
				add(player, new Item(13744, product.getQuantity()));
				return true;
			case "Elysian Spirit Shield":
				add(player, new Item(13742, product.getQuantity()));
				return true;
			case "Divine Spirit Shield":
				add(player, new Item(13740, product.getQuantity()));
				return true;
			case "Dragonfire Shield":
				add(player, new Item(11283, product.getQuantity()));
				return true;
			case "Bandos Chestplate":
				add(player, new Item(11724, product.getQuantity()));
				return true;
			case "Bandos Tassets":
				add(player, new Item(11726, product.getQuantity()));
				return true;
			case "Primordial Boots":
				add(player, new Item(43239, product.getQuantity()));
				return true;
			case "Pegasian Boots":
				add(player, new Item(43237, product.getQuantity()));
				return true;
			case "Eternal Boots":
				add(player, new Item(43235, product.getQuantity()));
				return true;
			case "Amulet of Fury":
				add(player, new Item(6585, product.getQuantity()));
				return true;
			case "Blood Necklace":
				add(player, new Item(17291, product.getQuantity()));
				return true;
			case "Armadyl Chestplate":
				add(player, new Item(11720, product.getQuantity()));
				return true;
			case "Armadyl Chainskirt":
				add(player, new Item(11722, product.getQuantity()));
				return true;
		}
		return false;
	}
}
