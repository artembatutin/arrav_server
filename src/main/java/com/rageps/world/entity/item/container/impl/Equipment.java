package com.rageps.world.entity.item.container.impl;

import com.google.common.collect.ImmutableSet;
import com.rageps.content.ShieldAnimation;
import com.rageps.content.item.Requirement;
import com.rageps.content.item.Skillcape;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.world.entity.actor.combat.CombatConstants;
import com.rageps.combat.listener.CombatListenerDispatcher;
import com.rageps.world.entity.actor.combat.weapon.WeaponAnimation;
import com.rageps.world.entity.actor.combat.weapon.WeaponInterface;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.item.container.ItemContainer;
import com.rageps.world.entity.item.container.ItemContainerAdapter;
import com.rageps.world.entity.item.container.ItemWeightListener;

import java.util.Optional;

/**
 * The container that manages the equipment for a player.
 * @author lare96 <http://github.com/lare96>
 */
public final class Equipment extends ItemContainer {
	
	/**
	 * An {@link ItemContainerAdapter} implementation that listens for changes to equipment.
	 */
	private final class EquipmentListener extends ItemContainerAdapter {
		
		/**
		 * Creates a new {@link EquipmentListener}.
		 */
		EquipmentListener() {
			super(player);
		}
		
		@Override
		public String getCapacityExceededMsg() {
			throw new IllegalStateException(EXCEPTION_MESSAGE);
		}
		
		@Override
		public void singleUpdate(ItemContainer container, Item oldItem, Item newItem, int slot, boolean update) {
			if(update)
				updateItem(container, newItem, slot);
			updateBonus(oldItem, newItem);
			writeBonuses();
		}
		
		@Override
		public void bulkUpdate(ItemContainer container) {
			updateItems(container);
			updateAllBonuses();
			writeBonuses();
		}
		
		@Override
		public int widget() {
			return EQUIPMENT_DISPLAY_ID;
		}
	}
	
	/**
	 * The size of all equipment instances.
	 */
	private static final int SIZE = 14;
	
	/**
	 * The equipment item display widget identifier.
	 */
	public static final int EQUIPMENT_DISPLAY_ID = 1688;
	
	/**
	 * The error message printed when certain functions from the superclass are utilized.
	 */
	private static final String EXCEPTION_MESSAGE = "Please use { equipment.set(index, Item) } instead";
	
	/**
	 * The head identification equipment slot.
	 */
	public static final int HEAD_SLOT = 0;
	
	/**
	 * The cape identification equipment slot.
	 */
	public static final int CAPE_SLOT = 1;
	
	/**
	 * The amulet identification equipment slot.
	 */
	public static final int AMULET_SLOT = 2;
	
	/**
	 * The weapon identification equipment slot.
	 */
	public static final int WEAPON_SLOT = 3;
	
	/**
	 * The chest identification equipment slot.
	 */
	public static final int CHEST_SLOT = 4;
	
	/**
	 * The shield identification equipment slot.
	 */
	public static final int SHIELD_SLOT = 5;
	
	/**
	 * The legs identification equipment slot.
	 */
	public static final int LEGS_SLOT = 7;
	
	/**
	 * The hands identification equipment slot.
	 */
	public static final int HANDS_SLOT = 9;
	
	/**
	 * The feet identification equipment slot.
	 */
	public static final int FEET_SLOT = 10;
	
	/**
	 * The ring identification equipment slot.
	 */
	public static final int RING_SLOT = 12;
	
	/**
	 * The arrows identification equipment slot.
	 */
	public static final int ARROWS_SLOT = 13;
	
	/**
	 * An {@link ImmutableSet} containing equipment indexes that don't require appearance updates.
	 */
	private static final ImmutableSet<Integer> NO_APPEARANCE = ImmutableSet.of(RING_SLOT, ARROWS_SLOT);
	
	/**
	 * The player who's equipment is being managed.
	 */
	private final Player player;
	
	/**
	 * The array of attack and defence bonus values.
	 */
	private final int[] bonuses = new int[18];
	
	/**
	 * Creates a new {@link Equipment}.
	 * @param player the player who's equipment is being managed.
	 */
	public Equipment(Player player) {
		super(SIZE, StackPolicy.STANDARD);
		this.player = player;
		addListener(new EquipmentListener());
		addListener(new ItemWeightListener(player));
	}
	
	@Override
	public int add(Item item, int preferredIndex, boolean refresh) {
		return -1;
	}
	
	@Override
	public int remove(Item item, int preferredIndex, boolean refresh) {
		return -1;
	}
	
	/**
	 * Equips an {@link Item} from the underlying player's {@link Inventory}.
	 * @param inventoryIndex The {@code Inventory} index to equip the {@code Item} from.
	 * @return {@code true} if the item was equipped, {@code false} otherwise.
	 */
	public boolean equip(int inventoryIndex) {
		if(inventoryIndex == -1)
			return false;
		Inventory inventory = player.getInventory();
		Item equipItem = inventory.get(inventoryIndex);
		if(equipItem == null)
			return false;
		if(!Item.valid(equipItem))
			return false;
		if(!Requirement.canEquip(player, equipItem))
			return false;
		if(!Skillcape.verifySkillCape(player, equipItem))
			return false;
		ItemDefinition def = equipItem.getDefinition();
		EquipmentType type = def.getEquipmentType();
		Optional<Item> unequipPrimary;
		Optional<Item> unequipSecondary = Optional.empty();
		if(type == EquipmentType.WEAPON) { // If we're equipping a 2h sword, unequip shield.
			unequipSecondary = def.isTwoHanded() && getItems()[SHIELD_SLOT] != null ? Optional.of(getItems()[SHIELD_SLOT]) : Optional.empty();
		} else if(type == EquipmentType.SHIELD) { // If we're equipping a shield while wearing a 2h sword, unequip sword.
			int weapon = computeIdForIndex(WEAPON_SLOT);
			boolean weaponTwoHanded = false;
			if(weapon != -1 && Item.valid(weapon)) {
				weaponTwoHanded = ItemDefinition.get(weapon).isTwoHanded();
			}
			unequipSecondary = weaponTwoHanded && getItems()[WEAPON_SLOT] != null ? Optional.of(getItems()[WEAPON_SLOT]) : Optional.empty();
		}
		
		//Stacking arrows if exist.
		if(getItems()[type.getSlot()] != null) {
			if(def.isStackable() && getItems()[type.getSlot()].getId() == equipItem.getId()) {
				equipItem = equipItem.createAndIncrement(getItems()[type.getSlot()].getAmount());
				unequipPrimary = Optional.empty();
			} else {
				unequipPrimary = Optional.of(getItems()[type.getSlot()]);
			}
		} else
			unequipPrimary = Optional.empty();
		
		//Just a check, had to put it in final.
		Item finalEquipItem = equipItem;
		if(!MinigameHandler.execute(player, m -> m.canEquip(player, finalEquipItem, finalEquipItem.getDefinition().getEquipmentType()))) {
			return false;
		}
		if(!player.getInventory().hasCapacityAfter(new Item[]{unequipPrimary.orElse(null), unequipSecondary.orElse(null)}, finalEquipItem)) {
			player.message("You don't have enough inventory space for this.");
			return false;
		}
		
		inventory.set(inventoryIndex, null, true);
		unequipPrimary.ifPresent(i -> this.unequip(i.getDefinition().getEquipmentType().getSlot(), player.getInventory(), true, inventoryIndex));
		unequipSecondary.ifPresent(i -> this.unequip(i.getDefinition().getEquipmentType().getSlot(), player.getInventory(), true, -1));
		set(type.getSlot(), equipItem, true);
		appearanceForIndex(type.getSlot());
		
		if(type == EquipmentType.SHIELD) {
			ShieldAnimation.execute(player, equipItem);
		}
		
		if(type == EquipmentType.ARROWS) {
			player.getCombat().reset(false, false);
		}
		
		if(type == EquipmentType.WEAPON) {
			WeaponInterface.execute(player, equipItem);
			WeaponAnimation.execute(player, equipItem);
			player.getCombat().reset(false, false);
			player.setAutocastSpell(null);
			player.send(new ConfigPacket(108, 0));
			player.send(new ConfigPacket(301, 0));
			player.setSpecialActivated(false);
		}
		
		CombatListenerDispatcher.CombatListenerSet listenerSet = CombatListenerDispatcher.ITEM_LISTENERS.get(equipItem.getId());
		
		if(listenerSet != null && player.getEquipment().containsAll(listenerSet.set)) {
			player.getCombat().addListener(listenerSet.listener);
		}
		return true;
	}
	
	/**
	 * Unequips an {@link Item} from the underlying player's {@code Equipment}.
	 * @param equipmentIndex The {@code Equipment} index to unequip the {@code Item} from.
	 * @return {@code true} if the item was unequipped, {@code false} otherwise.
	 */
	public boolean unequip(int equipmentIndex) {
		return unequip(equipmentIndex, player.getInventory(), true, -1);
	}
	
	/**
	 * Unequips an {@link Item} from the underlying player's {@code Equipment}.
	 * @param equipmentIndex The {@code Equipment} index to unequip the {@code Item} from.
	 * @param container The container to which we are putting the items on.
	 * @param refresh the condition if the container must be refreshed instantly.
	 * @param preferredSlot The slot id that may be preferred.
	 * @return {@code true} if the item was unequipped, {@code false} otherwise.
	 */
	public boolean unequip(int equipmentIndex, ItemContainer container, boolean refresh, int preferredSlot) {
		if(equipmentIndex == -1)
			return false;
		Item unequip = get(equipmentIndex);
		if(unequip == null) { // Item doesn't exist.
			return false;
		}
		if(!MinigameHandler.execute(player, m -> m.canUnequip(player, unequip, unequip.getDefinition().getEquipmentType())))
			return false;
		if(container == null || container.add(unequip, preferredSlot, refresh) >= 0) {
			set(equipmentIndex, null, refresh);
			appearanceForIndex(equipmentIndex);
			if(equipmentIndex == Equipment.SHIELD_SLOT) {
				player.setShieldAnimation(null);
			}
			if(equipmentIndex == Equipment.ARROWS_SLOT) {
				player.getCombat().reset(false, false);
			}
			if(equipmentIndex == Equipment.WEAPON_SLOT) {
				WeaponInterface.execute(player, null);
				WeaponAnimation.execute(player, new Item(0));
				player.getCombat().reset(false, false);
				player.setAutocastSpell(null);
				player.send(new ConfigPacket(108, 0));
				player.send(new ConfigPacket(301, 0));
				player.setSpecialActivated(false);
			}
			CombatListenerDispatcher.CombatListenerSet listenerSet = CombatListenerDispatcher.ITEM_LISTENERS.get(unequip.getId());
			
			if(listenerSet != null && !player.getEquipment().containsAll(listenerSet.set)) {
				player.getCombat().removeListener(listenerSet.listener);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Flags the {@code APPEARANCE} update block, only if the equipment piece on {@code equipmentIndex} requires an
	 * appearance update.
	 */
	private void appearanceForIndex(int equipmentIndex) {
		if(!NO_APPEARANCE.contains(equipmentIndex)) {
			player.getFlags().flag(UpdateFlag.APPEARANCE);
		}
	}
	
	/**
	 * Updates the bonuses array for single equipment index.
	 */
	private void updateBonus(Item oldItem, Item newItem) {
		int oldId = -1;
		int newId = -1;
		if(oldItem != null)
			oldId = oldItem.getId();
		if(newItem != null)
			newId = newItem.getId();
		if(oldId == newId) {
			return;
		}
		int[] oldBonuses = null;
		int[] newBonuses = null;
		if(oldItem != null && oldItem.getDefinition() != null)
			oldBonuses = oldItem.getDefinition().getBonus();
		if(newItem != null && newItem.getDefinition() != null)
			newBonuses = newItem.getDefinition().getBonus();
		if(oldBonuses != null || newBonuses != null) {
			int older = oldBonuses == null ? 0 : oldBonuses.length;
			int newer = newBonuses == null ? 0 : newBonuses.length;
			for(int i = 0; i < (older > newer ? older : newer); i++) {
				if(oldBonuses != null && oldBonuses.length > i)
					bonuses[i] -= oldBonuses[i];
				if(newBonuses != null && newBonuses.length > i)
					bonuses[i] += newBonuses[i];
			}
		}
	}
	
	/**
	 * Updates the bonuses array for all of the equipment indexes.
	 */
	private void updateAllBonuses() {
		for(int i = 0; i < bonuses.length; i++)
			bonuses[i] = 0;
		for(Item item : getItems()) {
			updateBonus(null, item);
		}
	}
	
	/**
	 * Writes a specific the bonus value on the equipment interface.
	 */
	private void writeBonuses() {
		for(int i = 0; i < bonuses.length; i++) {
			boolean percentage = (i >= 11 && i <= 13) || i == 17;
			player.interfaceText(CombatConstants.BONUS_IDS[i], CombatConstants.BONUS_NAMES[i] + ": " + (bonuses[i] >= 0 ? "+" : "") + bonuses[CombatConstants.BONUS[i]] + (percentage ? "%" : i == 14 ? ".0" : ""));
		}
	}
	
	/**
	 * @return The bonuses of all the equipment in this container.
	 */
	public int[] getBonuses() {
		return bonuses;
	}
	
}