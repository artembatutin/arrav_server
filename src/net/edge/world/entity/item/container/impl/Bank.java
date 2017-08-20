package net.edge.world.entity.item.container.impl;

import net.edge.net.packet.out.SendConfig;
import net.edge.net.packet.out.SendContainer;
import net.edge.net.packet.out.SendInventoryInterface;
import net.edge.world.entity.item.container.ItemContainer;
import net.edge.content.minigame.MinigameHandler;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

public final class Bank {

	/**
	 * The tab size.
	 */
	private final static int SIZE = 9;
	
	/**
	 * The main player to which this bank is associated to.
	 */
	private final Player player;
	
	/**
	 * Main bank tabs containers array.
	 */
	private final BankTab[] tabs = new BankTab[SIZE];
	
	/**
	 * Flag if initially all items were sent.
	 */
	private boolean bulkStartSent = false;
	
	/**
	 * The currently selected tab index.
	 */
	private int selectedTab;

	public Optional<Player> viewing = Optional.empty();

	public Bank(Player player) {
		this.player = player;
		this.selectedTab = 0;
		for(int i = 0; i < SIZE; i++) {
			tabs[i] = new BankTab(player, i, i == 0 ? 200 : 60);
		}
	}
	
	/**
	 * Copies a bank of a player to another one.
	 * @param player the player leeching the bank.
	 * @return A new instance of {@link Bank}.
	 */
	public Bank copy(Player player) {
		Bank b = new Bank(player);
		b.selectedTab = 0;
		for(int i = 0; i < SIZE; i++) {
			b.setItems(i, this.items(i));
		}
		return b;
	}
	
	/**
	 * Opens and refreshes the bank for {@code player}.
	 */
	public void open() {
		if(!MinigameHandler.execute(player, m -> m.canBank(player))) {
			return;
		}
		shiftAll();
		player.getAttr().get("banking").set(true);
		player.out(new SendConfig(115, player.getAttr().get("withdraw_as_note").getBoolean() ? 1 : 0));
		player.out(new SendConfig(116, player.getAttr().get("insert_item").getBoolean() ? 1 : 0));
		player.out(new SendInventoryInterface(-3, 5063));
		player.out(new SendContainer(5064, this.player.getInventory()));
		if(!bulkStartSent) {
			refreshAll();
			bulkStartSent = true;
		}
	}

	public void open(Player p) {
		if(!MinigameHandler.execute(player, m -> m.canBank(player))) {
			return;
		}

		Bank bank = p.getBank();

		p.getBank().shiftAll();

		player.getAttr().get("banking").set(true);
		player.out(new SendConfig(115, player.getAttr().get("withdraw_as_note").getBoolean() ? 1 : 0));
		player.out(new SendConfig(116, player.getAttr().get("insert_item").getBoolean() ? 1 : 0));
		player.out(new SendInventoryInterface(-3, 5063));
		player.out(new SendContainer(5064, this.player.getInventory()));

		viewing = Optional.of(p);

		updateViewer(player, bank, false);
	}

	private void updateViewer(Player viewing, Bank bank, boolean refreshInventory) {
		if(refreshInventory) {
			viewing.getInventory().refreshBulk(player, BankTab.BANKING_INVENTORY);
			viewing.getInventory().updateBulk();
		}

		for(int i = 0; i < SIZE; i++) {
			if(bank.tabs[i] == null) {
				continue;
			}

			viewing.out(new SendContainer(270 + i, bank.tabs[i]));
		}
	}

	/**
	 * Deposits a newly created item into the bank.
	 */
	public int deposit(Item item) {
		return tabs[getSelectedTab()].add(item);
	}

	/**
	 * Withdraws an item from this banking tab from the {@code bankSlot} slot. This is
	 * used for when a player is manually withdrawing an item using the banking tab
	 * interface.
	 * @param bankSlot the slot from the player's banking tab.
	 * @param amount   the amount of the item being withdrawn.
	 * @return {@code true} if the item was withdrawn, {@code false} otherwise.
	 */
	public boolean withdraw(Player player, int tab, int bankSlot, int amount) {
		boolean withdrawed = viewing.isPresent() ? viewing.get().getBank().tabs[tab].withdraw(player, bankSlot, amount, false) : tabs[tab].withdraw(player, bankSlot, amount, true);
		viewing.ifPresent(v -> {
			if(!withdrawed) {
				return;
			}
			updateViewer(player, v.getBank(), true);
			v.message(player.getFormatUsername() + " has just removed an item from ur bank!");
		});
		return withdrawed;
	}
	
	/**
	 * Handles the item tab switching in the bank.
	 * @param tab     the tab being grabbed from.
	 * @param slot    the slot of the item grabbed.
	 * @param nextTab the tab moving to.
	 */
	public boolean tabTransfer(int tab, int slot, int nextTab) {
		if(nextTab >= 0 && nextTab <= 9) {
			Item item = tabs[tab].get(slot);
			if(item != null) {
				if(tabs[nextTab].canAdd(item) && tabs[tab].canRemove(item)) {
					tabs[tab].remove(item);
					tabs[nextTab].add(item);
					tabs[tab].shifting(player);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Transfers the item in {@code slot} to {@code newSlot}. If an item already
	 * exists in the new slot, the items in this container will be shifted to
	 * accommodate for the transfer.
	 * @param tab     the selected tab sent by the client.
	 * @param slot    the slot of the item to transfer.
	 * @param newSlot the slot to transfer the item to.
	 * @return {@code true} if the transfer was successful, {@code false}
	 * otherwise.
	 */
	public boolean transfer(int tab, int slot, int newSlot) {
		return tabs[tab].transfer(slot, newSlot);
	}
	
	/**
	 * Swaps the positions of two items in this container.
	 * @param tab       the selected tab sent by the client.
	 * @param slot      the slot of the first item to swap.
	 * @param otherSlot the slot of the second item to swap.
	 */
	public void swap(int tab, int slot, int otherSlot) {
		tabs[tab].swap(slot, otherSlot);
	}
	
	/**
	 * Deposits an item to this bank that currently exists in a specified {@link ItemContainer}.
	 * This is used for when a player is manually depositing an item using the banking tab interface.
	 * @param slot    the slot from the specified {@link ItemContainer}.
	 * @param amount  the amount of the item being deposited.
	 * @param from    The container we are withdrawing from.
	 * @param refresh The flag to determine if we refresh the container.
	 * @return {@code true} if the item was deposited, {@code false} otherwise.
	 */
	public boolean deposit(int slot, int amount, ItemContainer from, boolean refresh) {
		return from.get(slot) != null && tabs[getSelectedTab()].deposit(player, slot, amount, refresh, from);
	}
	
	/**
	 * Attemps to add an item to the selected tab.
	 * @param slot the tab slot to be added to.
	 * @param item the item to be added to.
	 * @return {@code true} if it went successful, {@code false} otherwise.
	 */
	public int add(int slot, Item item) {
		return tabs[slot].add(item);
	}
	
	/**
	 * Seeks to find if an item exists in a tab.
	 * @param item the item looking for.
	 * @return the index of the tab found, if none then -1.
	 */
	public int contains(Item item) {
		int id = item.getDefinition().isNoted() ? item.getId() - 1 : item.getId();
		for(BankTab t : tabs) {
			if(t.contains(id))
				return t.getSlot();
		}
		return -1;
	}
	
	/**
	 * Seeks to find if an item exists in a tab.
	 * @param item the item id looking for.
	 * @return the index of the tab found, if none then -1.
	 */
	public int contains(int item) {
		for(BankTab t : tabs) {
			if(t.contains(item))
				return t.getSlot();
		}
		return 0;
	}
	
	/**
	 * Refreshes the contents of this bank tab container to the interface.
	 */
	public void refresh() {
		tabs[getSelectedTab()].updateBulk();
	}
	
	/**
	 * Refreshes the contents of this bank tab container to the interface.
	 */
	public void refreshAll() {
		for(BankTab t : tabs) {
			t.updateBulk();
		}
	}
	
	/**
	 * Shifts all items in this container to the left to fill any {@code null} slots.
	 */
	public void shift() {
		tabs[getSelectedTab()].shift();
	}
	
	/**
	 * Shifts all items in this container to the left to fill any {@code null} slots.
	 */
	public void shiftAll() {
		boolean force = player.getAttr().get("shifting_req").getBoolean();
		for(BankTab t : tabs) {
			if(force || t.isShiftingReq())
				t.shift();
		}
		player.getAttr().get("shifting_req").set(false);
	}
	
	/**
	 * Refreshes the contents of this bank container to the interface.
	 */
	public void clear() {
		for(BankTab t : tabs)
			t.clear();
	}
	
	/**
	 * Gets the total quantity of all items with {@code id}.
	 * @param id the item identifier to retrieve the total quantity of.
	 * @return the total quantity of items in this container with the
	 * identifier.
	 */
	public int amount(int id) {
		return tabs[getSelectedTab()].computeAmountForId(id);
	}
	
	/**
	 * Sets the container of items to {@code items}. The container will not hold
	 * any references to the array, nor the item instances in the array.
	 * @param i     the index of the tab to be selected.
	 * @param items the new array of items, the capacities of this must be equal
	 *              to or lesser than the container.
	 */
	public final void setItems(int i, Item[] items) {
		tabs[i].fillItems(items);
	}
	
	/**
	 * Sets the container of items to {@code items}. The container will not hold
	 * any references to the array, nor the item instances in the array.
	 * @param items the new array of items, the capacities of this must be equal
	 *              to or lesser than the container.
	 */
	public final void setItems(Item[] items) {
		tabs[getSelectedTab()].fillItems(items);
	}
	
	/**
	 * Gets the {@link ItemContainer} depending on the index.
	 * @param index the index to get the items from.
	 * @return the items contained within this container.
	 */
	public final ItemContainer container(int index) {
		return tabs[index];
	}
	
	/**
	 * Gets the items in an array format.
	 * @param index the index to get the items from.
	 * @return the items contained within this container.
	 */
	public final Item[] items(int index) {
		return tabs[index].getItems();
	}
	
	/**
	 * Gets the selected tab slot.
	 * @return selected tab slot.
	 */
	public int getSelectedTab() {
		return selectedTab;
	}
	
	/**
	 * The the new value for {@link #selectedTab}.
	 * @param selectedTab the new value to set.
	 */
	public void setTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}
	
	/**
	 * Deposits a whole {@code Inventory} directly into the selected tab.
	 */
	public void depositeInventory() {
		tabs[getSelectedTab()].depositeInventory(player);
	}
	
	/**
	 * Deposits a whole {@code Equipment} directly into the selected tab.
	 */
	public void depositeEquipment() {
		tabs[getSelectedTab()].depositeEquipment(player);
	}
	
	/**
	 * Deposits a whole {@code FamiliarContainer} directly into the selected tab.
	 */
	public void depositeFamiliar() {
		tabs[getSelectedTab()].depositeFamiliar(player);
	}
	
}
