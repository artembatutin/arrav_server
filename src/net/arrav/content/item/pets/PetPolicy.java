package net.arrav.content.item.pets;

import net.arrav.world.entity.item.Item;

import java.util.Optional;

public final class PetPolicy {
	
	private final Item item;
	
	private final int npcId;
	
	private final boolean last;
	
	private final Optional<PetData> next;
	
	PetPolicy(int itemId, int npcId, PetData next) {
		this.item = new Item(itemId);
		this.npcId = npcId;
		this.next = next == null ? Optional.empty() : Optional.of(next);
		this.last = next == null;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public Optional<PetData> getNext() {
		return next;
	}
	
	public boolean isLast() {
		return last;
	}
	
}
