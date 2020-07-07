package com.rageps.net.rest.payments;

import com.rageps.net.rest.payments.impl.CosmeticProductReceivedListener;
import com.rageps.net.rest.payments.impl.MiscellaneousProductReceivedListener;
import com.rageps.net.rest.payments.impl.ShieldsAmuletsProductReceivedListener;
import com.rageps.net.rest.payments.impl.WeaponsProductReceivedListener;
import com.rageps.world.entity.actor.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ryley Kimmel on 10/30/2016.
 */
public final class Products {
	private final Set<ProductReceivedListener> products = new HashSet<>();

	public Products() {
		init();
	}

	public void init() {
		products.add(new MiscellaneousProductReceivedListener());
		products.add(new CosmeticProductReceivedListener());
		products.add(new WeaponsProductReceivedListener());
		products.add(new ShieldsAmuletsProductReceivedListener());
	}

	public void receive(Player player, Product product) {
		boolean received = false;
		for (ProductReceivedListener listener : products) {
			if (listener.receive(player, product)) {
				received = true;
				break;
			}
		}

		if (!received) {
			throw new UnsupportedOperationException("nexus product listener for product: " + product + " does not exist");
		}
	}

}
