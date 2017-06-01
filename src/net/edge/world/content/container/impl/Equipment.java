package net.edge.world.content.container.impl;

import com.google.common.collect.ImmutableSet;
import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.weapon.WeaponAnimation;
import net.edge.world.content.combat.weapon.WeaponInterface;
import net.edge.world.content.container.ItemContainer;
import net.edge.world.content.container.ItemContainerAdapter;
import net.edge.world.content.container.ItemWeightListener;
import net.edge.world.content.item.Requirement;
import net.edge.world.content.item.Skillcape;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.ShieldAnimation;
import net.edge.world.node.entity.update.UpdateFlag;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static net.edge.world.content.container.impl.EquipmentType.WEAPON;

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
		public int getWidgetId() {
			return EQUIPMENT_DISPLAY_ID;
		}
		
		@Override
		public String getCapacityExceededMsg() {
			throw new IllegalStateException(EXCEPTION_MESSAGE);
		}
		
		@Override
		public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh) {
			if(refresh)
				sendItemsToWidget(container);
			updateBonus(oldItem, newItem);
			writeBonuses();
		}
		
		@Override
		public void bulkItemsUpdated(ItemContainer container) {
			sendItemsToWidget(container);
			updateAllBonuses();
			writeBonuses();
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
	public boolean add(Item item, int preferredIndex, boolean refresh) {
		return true;
	}
	
	@Override
	public boolean remove(Item item, int preferredIndex, boolean refresh) {
		return true;
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
		if(type == WEAPON) { // If we're equipping a 2h sword, unequip shield.
			unequipSecondary = def.isTwoHanded() && getItems()[SHIELD_SLOT] != null ? Optional.of(getItems()[SHIELD_SLOT]) : Optional.empty();
		} else if(type == EquipmentType.SHIELD) { // If we're equipping a shield while wearing a 2h sword, unequip sword.
			boolean weaponTwoHanded = computeIdForIndex(WEAPON_SLOT).
					map(ItemDefinition::get).
					map(ItemDefinition::isTwoHanded).
					orElse(false);
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
		if(!MinigameHandler.execute(player, m -> m.canEquip(player, finalEquipItem, finalEquipItem.getDefinition().getEquipmentType())))
			return false;
		
		if(!inventory.hasCapacityFor(unequipPrimary.orElse(null), unequipSecondary.orElse(null))) {
			boolean possible = false;
			if(unequipPrimary.isPresent() && unequipSecondary.isPresent()) {
				if(equipItem.getDefinition().isTwoHanded() && unequipPrimary.get().getDefinition().getEquipmentType().getSlot() == type.getSlot())
					possible = true;
			} else if(unequipPrimary.isPresent()) {//one item
				if(unequipPrimary.get().getDefinition().getEquipmentType().getSlot() == type.getSlot())
					possible = true;
			}
			if(!possible) {
				player.message("You do not have enough space in your inventory.");
				return false;
			}
		}
		
		inventory.set(inventoryIndex, null, true);
		unequipPrimary.ifPresent(i -> this.unequip(i.getDefinition().getEquipmentType().getSlot(), player.getInventory(), true));
		unequipSecondary.ifPresent(i -> this.unequip(i.getDefinition().getEquipmentType().getSlot(), player.getInventory(), true));
		set(type.getSlot(), equipItem, true);
		appearanceForIndex(type.getSlot());
		
		if(type == EquipmentType.SHIELD) {
			ShieldAnimation.execute(player, equipItem);
		}
		if(type == WEAPON && def.isWeapon()) {
			WeaponInterface.execute(player, equipItem);
			WeaponAnimation.execute(player, equipItem);
			player.setCastSpell(null);
			player.setAutocastSpell(null);
			player.setAutocast(false);
			player.getMessages().sendConfig(108, 0);
			player.getMessages().sendConfig(301, 0);
			player.setSpecialActivated(false);
		}
		return true;
	}
	
	/**
	 * Unequips an {@link Item} from the underlying player's {@code Equipment}.
	 * @param equipmentIndex The {@code Equipment} index to unequip the {@code Item} from.
	 * @return {@code true} if the item was unequipped, {@code false} otherwise.
	 */
	public boolean unequip(int equipmentIndex) {
		return unequip(equipmentIndex, player.getInventory(), true);
	}
	
	/**
	 * Unequips an {@link Item} from the underlying player's {@code Equipment}.
	 * @param equipmentIndex The {@code Equipment} index to unequip the {@code Item} from.
	 * @param container      The container to which we are putting the items on.
	 * @param refresh        the condition if the container must be refreshed instantly.
	 * @return {@code true} if the item was unequipped, {@code false} otherwise.
	 */
	public boolean unequip(int equipmentIndex, ItemContainer container, boolean refresh) {
		if(equipmentIndex == -1)
			return false;
		Item unequip = get(equipmentIndex);
		if(unequip == null) { // Item doesn't exist.
			return false;
		}
		if(!MinigameHandler.execute(player, m -> m.canUnequip(player, unequip, unequip.getDefinition().getEquipmentType())))
			return false;
		if(container.add(unequip, -1, refresh)) {
			set(equipmentIndex, null, refresh);
			appearanceForIndex(equipmentIndex);
			if(equipmentIndex == Equipment.SHIELD_SLOT) {
				player.setShieldAnimation(null);
			}
			if(equipmentIndex == Equipment.WEAPON_SLOT) {
				WeaponInterface.execute(player, null);
				player.setCastSpell(null);
				player.setAutocastSpell(null);
				player.setAutocast(false);
				player.getMessages().sendConfig(108, 0);
				WeaponAnimation.execute(player, new Item(0));
				player.getMessages().sendConfig(301, 0);
				player.setSpecialActivated(false);
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
	private void updateBonus(Optional<Item> oldItem, Optional<Item> newItem) {
		Optional<Integer> oldId = oldItem.map(Item::getId);
		Optional<Integer> newId = newItem.map(Item::getId);
		if(oldId.equals(newId)) {
			return;
		}
		IntStream indexes = IntStream.range(0, bonuses.length);
		applyBonuses(oldItem).ifPresent(it -> indexes.forEach(index -> bonuses[index] -= it[index]));
		applyBonuses(newItem).ifPresent(it -> indexes.forEach(index -> bonuses[index] += it[index]));
	}
	
	/**
	 * Takes an {@code Optional<Item>} and returns a {@code ImmutableList<Integer>} from it. Used under-the-hood to reduce
	 * boilerplate.
	 */
	private Optional<int[]> applyBonuses(Optional<Item> item) {
		return item.map(Item::getDefinition).map(ItemDefinition::getBonus);
	}
	
	/**
	 * Updates the bonuses array for all of the equipment indexes.
	 */
	private void updateAllBonuses() {
		Arrays.fill(bonuses, 0);
		stream().filter(Objects::nonNull).forEach(it -> updateBonus(Optional.empty(), Optional.of(it)));
	}
	
	/**
	 * Writes a specific the bonus value on the equipment interface.
	 */
	private void writeBonuses() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bonuses.length; i++) {
			boolean percentage = (i >= 11 && i <= 13) || i == 17;
			player.getMessages().sendString(Combat.BONUS_NAMES[i] + ": " + (bonuses[i] >= 0 ? "+" : "") + bonuses[Combat.BONUS[i]] + (percentage ? "%" : i == 14 ? ".0" : ""), Combat.BONUS_IDS[i]);
			sb.setLength(0);
		}
	}
	
	/**
	 * Forces a refresh of {@code Equipment} items to the {@code EQUIPMENT_DISPLAY_ID} widget.
	 */
	private void forceRefresh() {
		refresh(player, EQUIPMENT_DISPLAY_ID);
	}
	
	/**
	 * @return The bonuses of all the equipment in this container.
	 */
	public int[] getBonuses() {
		return bonuses;
	}
	
}