package net.arrav.util.eco.impl;

import net.arrav.util.eco.ItemController;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.ItemDefinition;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemEconomyWipe extends ItemController {

	private final int item;

	private int amount;

	public ItemEconomyWipe(int item) {
		this.item = item;
	}

	@Override
	public Item change(Item item) throws IOException {
		if(item.getId() == this.item) {
			pOut();
			amount += item.getAmount();
			return null;
		}
		return item;
	}

	@Override
	public void newPlayer() throws IOException {
		out().write(amount + " amount wiped");
		amount = 0;
	}

	@Override
	public String getName() {
		return "wipe" + item;
	}

	@Override
	public String getDesc() {
		return "[ wiping item " + ItemDefinition.get(item).getName() + " - " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " ]";
	}

}
