package com.rageps.net.rest.payments;

import com.google.common.base.MoreObjects;

/**
 * Created by Ryley Kimmel on 12/3/2017.
 */
public final class Product {

	private final String itemName;

	private final int quantity;

	public Product(String itemName, int quantity) {
		this.itemName = itemName;
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("itemName", itemName).add("quantity", quantity).toString();
	}
}
