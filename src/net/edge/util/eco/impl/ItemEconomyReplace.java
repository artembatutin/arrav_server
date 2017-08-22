package net.edge.util.eco.impl;

import net.edge.util.eco.ItemController;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemEconomyReplace extends ItemController {
	
	private final int item, replace;
	
	private int amount;
	
	public ItemEconomyReplace(int item, int replace) {
		this.item = item;
		this.replace = replace;
	}
	
	@Override
	public Item change(Item item) throws IOException {
		if(item.getId() == this.item) {
			pOut();
			return null;
		}
		return item;
	}
	
	@Override
	public void newPlayer() throws IOException {
		out().write(amount + " amount replaced");
		amount = 0;
	}
	
	@Override
	public String getName() {
		return "replace" + item;
	}
	
	@Override
	public String getDesc() {
		return "[ replacing item " + item + " " + ItemDefinition.get(item).getName() + " with " + replace + " " +
				ItemDefinition.get(item).getName() + " - " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " ]";
	}
	
}
