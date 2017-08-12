package net.edge.world.entity.item.container.impl;

import net.edge.net.packet.out.SendContainer;
import net.edge.world.entity.item.container.ItemContainer;
import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.IndexedItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;
import net.edge.world.entity.item.container.ItemContainerAdapter;

import java.util.OptionalInt;

/**
 * An implementation of a single{@link ItemContainer} bank tab.
 */
final class BankTab extends ItemContainer {
	
	/**
	 * The inventory item display widget identifier.
	 */
	private static final int BANKING_INVENTORY = 5064;
	
	/**
	 * The slot of this tab in the bank.
	 */
	private final int slot;
	
	/**
	 * Flag if shifting is required.
	 */
	private boolean shiftingReq = true;
	
	/**
	 * Creates a new bank tab.
	 * @param player   the player instance.
	 * @param slot     the slot of our bank tab.
	 * @param capacity the capacity of our bank tab.
	 */
	BankTab(Player player, int slot, int capacity) {
		super(capacity, StackPolicy.ALWAYS);
		addListener(new BankListener(player, slot));
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
		
		if(canAdd(newDepositItem) && container.canRemove(depositItem)) {
			add(newDepositItem, -1, refresh);
			boolean p = container.remove(depositItem, inventoryIndex, refresh) != -1;
			if(refresh)
				forceRefresh(player);
			return p;
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
		if(canRemove(withdrawItem) && inventory.canAdd(newWithdrawItem)) {
			if(remove(withdrawItem, -1, refresh) > 0) {
				shiftingReq = true;
			}
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
		player.out(new SendContainer(5064, inv));
		player.out(new SendContainer(3214, inv));
		forceRefresh(player);
	}
	
	/**
	 * Deposits a whole {@code Equipment} directly into the tab.
	 */
	void depositeEquipment(Player player) {
		Equipment equip = player.getEquipment();
		for(IndexedItem i : equip.toIndexedArray()) {
			equip.unequip(i.getIndex(), player.getBank().container(player.getBank().contains(i.getId())), false, -1);
		}
		forceRefresh(player);
		equip.updateBulk();
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
				container.refreshBulk(player, 2702);
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
		inventory.refreshBulk(player, BANKING_INVENTORY);
		inventory.updateBulk();
		updateBulk();
	}
	
	/**
	 * Gets the bank tab slot.
	 * @return bank tab slot.
	 */
	public int getSlot() {
		return slot;
	}
	
	/**
	 * Condition if shifting is required.
	 * @return flag.
	 */
	public boolean isShiftingReq() {
		return shiftingReq;
	}
	
	/**
	 * Sets the {@link #shiftingReq} condition.
	 */
	public void shifting(Player player) {
		this.shiftingReq = true;
		player.getAttr().get("shifting_req").set(true);
	}
	
	/**
	 * An {@link ItemContainerAdapter} implementation that listens for changes to the bank.
	 */
	public static class BankListener extends ItemContainerAdapter {
		
		/**
		 * The slot of this bank listener.
		 */
		private final int slot;
		
		/**
		 * Creates a new {@link BankListener}.
		 */
		public BankListener(Player player, int slot) {
			super(player);
			this.slot = slot;
		}
		
		@Override
		public int widget() {
			return 270 + slot;
		}
		
		@Override
		public String getCapacityExceededMsg() {
			return "You do not have enough bank space to deposit that.";
		}
	}
}
