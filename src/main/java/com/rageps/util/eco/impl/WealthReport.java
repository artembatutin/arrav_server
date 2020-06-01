package com.rageps.util.eco.impl;

import com.rageps.content.market.MarketItem;
import com.rageps.util.eco.ItemController;
import com.rageps.world.entity.item.Item;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WealthReport extends ItemController {
	
	private int wealth;
	
	@Override
	public Item change(Item item) throws IOException {
		MarketItem val = MarketItem.get(item.getId());
		if(val != null) {
			wealth += val.getPrice();
		}
		return item;
	}
	
	@Override
	public void newPlayer() throws IOException {
		out().write(wealth + " total wealth");
		out().newLine();
		wealth = 0;
	}
	
	@Override
	public String getName() {
		return "wipe" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
	
	@Override
	public String getDesc() {
		return "[ wealth check up ]";
	}
	
}
