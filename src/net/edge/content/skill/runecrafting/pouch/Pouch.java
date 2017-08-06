package net.edge.content.skill.runecrafting.pouch;

import net.edge.world.entity.item.Item;

public final class Pouch {

	private int amount;
	private int id;

	public Pouch(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public int getAmount() {
		return amount;
	}
	
	
	
}
