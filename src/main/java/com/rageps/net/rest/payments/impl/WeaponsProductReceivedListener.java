package com.rageps.net.rest.payments.impl;

import com.rageps.net.rest.payments.Product;
import com.rageps.net.rest.payments.ProductReceivedListener;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Created by sALOON on 8/25/2017.
 */
public final class WeaponsProductReceivedListener implements ProductReceivedListener {
	@Override
	public boolean receive(Player player, Product product) {
		switch (product.getItemName()) {
			case "Abyssal Whip":
				add(player, new Item(4151, product.getQuantity()));
				return true;
			case "Kraken Tentacle":
				add(player, new Item(42004, product.getQuantity()));
				return true;
			case "Armadyl Godsword":
				add(player, new Item(11694, product.getQuantity()));
				return true;
			case "Chaotic Rapier":
				add(player, new Item(18349, product.getQuantity()));
				return true;
			case "Chaotic Maul":
				add(player, new Item(18353, product.getQuantity()));
				return true;
			case "Chaotic Staff":
				add(player, new Item(18355, product.getQuantity()));
				return true;
			case "Chaotic Crossbow":
				add(player, new Item(18357, product.getQuantity()));
				return true;
			case "Chaotic Longsword":
				add(player, new Item(18351, product.getQuantity()));
				return true;
			case "Dragon Claws":
				add(player, new Item(14484, product.getQuantity()));
				return true;
			case "Primal Rapier":
				add(player, new Item(16955, product.getQuantity()));
				return true;
			case "Primal Longsword":
				add(player, new Item(16403, product.getQuantity()));
				return true;
			case "Primal Maul":
				add(player, new Item(16425, product.getQuantity()));
				return true;
			case "Primal 2H":
				add(player, new Item(16909, product.getQuantity()));
				return true;
			case "Abyssal Dagger":
				add(player, new Item(43265, product.getQuantity()));
				return true;
			case "Abyssal Bludgeon":
				add(player, new Item(43263, product.getQuantity()));
				return true;
			case "Toxic Blowpipe":
				add(player, new Item(42924, product.getQuantity()));
				return true;
			case "Infernal Cape":
				return true;
		}
		return false;
	}
}
