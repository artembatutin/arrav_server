package net.edge.content.container.impl.bank;

import net.edge.content.container.ItemContainer;
import net.edge.content.container.impl.Equipment;
import net.edge.content.container.impl.Inventory;
import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.IndexedItem;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

import java.util.OptionalInt;

/**
 * An implementation of a single{@link ItemContainer} bank tab.
 */
final class BankTab extends ItemContainer {
	
	/**
	 * The slot of this tab in the bank.
	 */
	private final int slot;
	
	/**
	 * Creates a new bank tab.
	 * @param player   the player instance.
	 * @param slot     the slot of our bank tab.
	 * @param capacity the capacity of our bank tab.
	 */
	BankTab(Player player, int slot, int capacity) {
		super(capacity, StackPolicy.ALWAYS);
		addListener(new BankListener(player));
		this.slot = slot;
	}
	
	/**
	 * Deposits an {@link Item} from the underlying player's {@link Inventory}.
	 * @param player         The player depositing an item.
	 * @param inventoryIndex The {@code Inventory} index that the {@code Item} will be deposited from.
	 * @param amount         The amount of the {@code Item} to deposit.
	 * @param refresh        The flag if the tab must be refreshed after the operation.
	 * @param container      The container from which we will withdraw.
	 * @return {@code true} if the {@code Item} was successfully deposited, {@code false} otherwise.
	 */
	boolean deposit(Player player, int inventoryIndex, int amount, boolean refresh, ItemContainer container) {
		Item depositItem = container.get(inventoryIndex);
		
		if(depositItem == null || amount < 1) { // Item doesn't exist in inventory.
			return false;
		}
		
		int existingAmount = container.computeAmountForId(depositItem.getId());
		if(amount > existingAmount) { // Deposit amount is more than we actually have, size it down.
			amount = existingAmount;
		}
		depositItem = depositItem.createWithAmount(amount);
		
		ItemDefinition def = depositItem.getDefinition();
		Item newDepositItem = depositItem.createWithId(def.isNoted() ? def.getNoted() : depositItem.getId());
		
		int remaining = remaining(); // Do we have enough space in the bank?
		if(remaining < 1 && computeIndexForId(newDepositItem.getId()) == -1) {
			fireCapacityExceededEvent();
			return false;
		}
		
		if(add(newDepositItem, -1, refresh)) {
			container.remove(depositItem, inventoryIndex, refresh);
			if(refresh)
				forceRefresh(player);
			return true;
		}
		return false;
	}
	
	/**
	 * Withdraws an {@link Item} from the underlying player's {@code Bank}.
	 * @param bankIndex The {@code Bank} index that the {@code Item} will be deposited from.
	 * @param amount    The amount of the {@code Item} to withdraw.
	 * @param refresh   the flag if the container must be sent as a packet.
	 * @return {@code true} if the {@code Item} was successfully deposited, {@code false} otherwise.
	 */
	boolean withdraw(Player player, int bankIndex, int amount, boolean refresh) {
		Inventory inventory = player.getInventory();
		Item withdrawItem = get(bankIndex);
		
		if(withdrawItem == null || amount < 1) { // Item doesn't exist in bank.
			return false;
		}
		int existingAmount = withdrawItem.getAmount();
		if(amount > existingAmount) { // Withdraw amount is more than we actually have, size it down.
			amount = existingAmount;
		}
		
		OptionalInt newId = OptionalInt.empty();
		if(player.getAttr().get("withdraw_as_note").getBoolean()) { // Configure the noted id of the item we're withdrawing, if applicable.
			ItemDefinition def = withdrawItem.getDefinition();
			if(def.isNoteable()) {
				newId = OptionalInt.of(def.getNoted());
			} else {
				player.message("This item cannot be withdrawn as a note.");
			}
		}
		
		Item newWithdrawItem = withdrawItem.createWithId(newId.orElse(withdrawItem.getId()));
		ItemDefinition newDef = newWithdrawItem.getDefinition();
		
		int remaining = inventory.remaining();
		if(amount > remaining && !newDef.isStackable()) { // Size down withdraw amount to inventory space.
			amount = remaining;
		}
		withdrawItem = withdrawItem.createWithAmount(amount);
		newWithdrawItem = newWithdrawItem.createWithAmount(amount);
		
		boolean inventorySpace = inventory.hasCapacityFor(newWithdrawItem);
		if(!inventorySpace) {
			inventory.fireCapacityExceededEvent();
			return false;
		}
		if(remove(withdrawItem, -1, refresh)) {
			inventory.add(newWithdrawItem, -1, refresh);
			if(refresh)
				forceRefresh(player);
			return true;
		}
		return false;
	}
	
	/**
	 * Deposits a whole {@code Inventory} directly into the tab.
	 */
	void depositeInventory(Player player) {
		Inventory inv = player.getInventory();
		for(IndexedItem i : inv.toIndexedArray()) {
			if(player.getBank().deposit(i.getIndex(), i.getAmount(), inv, false)) {
				inv.remove(new Item(i.getId(), i.getAmount()), i.getIndex(), false);
			}
		}
		player.getMessages().sendItemsOnInterface(5064, inv.toArray());
		player.getMessages().sendItemsOnInterface(3214, inv.toArray());
		forceRefresh(player);
	}
	
	/**
	 * Deposits a whole {@code Equipment} directly into the tab.
	 */
	void depositeEquipment(Player player) {
		Equipment equip = player.getEquipment();
		for(IndexedItem i : equip.toIndexedArray()) {
			equip.unequip(i.getIndex(), player.getBank().container(player.getBank().contains(i.getId())), false);
		}
		forceRefresh(player);
		equip.refresh();
	}
	
	/**
	 * Deposits a whole {@code FamiliarContainer} directly into the tab.
	 */
	void depositeFamiliar(Player player) {
		player.getFamiliar().ifPresent(f -> {
			if(f.getAbilityType().isHoldableContainer()) {
				ItemContainer container = ((FamiliarContainer) f.getAbilityType()).getContainer();
				for(IndexedItem i : container.toIndexedArray()) {
					if(player.getBank().deposit(i.getIndex(), i.getAmount(), container, false)) {
						container.remove(new Item(i.getId(), i.getAmount()), i.getIndex(), false);
					}
				}
				container.refresh(player, 2702);
				forceRefresh(player);
			}
		});
	}
	
	/**
	 * Forces a refresh of {@code Bank} items to the {@code BANK_DISPLAY_ID} widget and {@link Inventory} items to the {@code
	 * INVENTORY_DISPLAY_ID} widget.
	 */
	private void forceRefresh(Player player) {
		Inventory inventory = player.getInventory();
		inventory.refresh(player);
		refresh(player, 270 + slot);
	}
	
	/**
	 * Sends the items on the interface.
	 */
	@Override
	public void refresh(Player player) {
		player.getMessages().sendItemsOnInterface(270 + slot, toArray());
	}
	
	public int widget() {
		return isTest() ? -1 : 270 + slot;
	}
	
	/**
	 * Gets the bank tab slot.
	 * @return bank tab slot.
	 */
	public int getSlot() {
		return slot;
	}
}
