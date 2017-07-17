package net.edge.content.skill.construction;

import net.edge.locale.Position;
import net.edge.world.node.actor.mob.impl.DefaultMob;
import net.edge.world.node.item.Item;

/**
 * Represents a {@link House} servant.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Servant extends DefaultMob {
	
	private boolean fetching, greetVisitors;
	private Item[] inventory;
	
	public Servant(int npcId, Position position) {
		super(npcId, position);
	}
	
	public Servant(int npcId, Position position, int inventorySize) {
		super(npcId, position);
		inventory = new Item[inventorySize];
	}
	
	/*public boolean addInventoryItem(int itemId) {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == 0) {
				inventory[i] = itemId;
				return true;
			}
		}
		return false;
	}
	
	public int freeSlots() {
		int value = 0;
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == 0) {
				value++;
			}
		}
		return value;
	}
	
	public void putBackInBank(Player p) {
		p.setBanking(true);
		for(int i = 0; i < inventory.length; i++) {
			if(i <= 0)
				continue;
			int tab = Bank.getTabForItem(p, inventory[i]);
			p.getBank(tab).add(inventory[i], 1);
		}
		p.setBanking(false);
	}
	
	public void takeItemsFromBank(Player p, int itemId, int amount) {
		for(int i = 0; i < amount; i++) {
			if(freeSlots() == 0)
				return;
			int tab = Bank.getTabForItem(p, inventory[i]);
			if(!p.getBank(tab).contains(itemId))
				return;
			if(addInventoryItem(itemId))
				p.getBank(tab).delete(itemId + 1, 1);
		}
	}
	
	public void giveItems(Player p) {
		p.getInventory().addOrBank(inventory);
		inventory = new Item[inventory.length];
	}*/
	
	public Item[] getInventory() {
		return inventory;
	}
	
	public boolean isFetching() {
		return fetching;
	}
	
	public void setFetching(boolean fetching) {
		this.fetching = fetching;
	}
	
	public boolean isGreetVisitors() {
		return greetVisitors;
	}
	
	public void setGreetVisitors(boolean greetVisitors) {
		this.greetVisitors = greetVisitors;
	}
	
}