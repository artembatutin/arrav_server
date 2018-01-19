package net.arrav.world.entity.actor.combat.magic;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.ItemContainer;
import net.arrav.world.entity.item.container.impl.Equipment;
import net.arrav.world.entity.item.container.impl.Inventory;

import java.util.LinkedList;
import java.util.List;

public enum MagicRune {

	AIR_RUNE(556, new int[]{4695, 4696, 4697}, new int[]{1381, 1397, 1405, 11998, 12000, 20730, 20733, 20736, 20739}),
	FIRE_RUNE(554, new int[]{4694, 4697, 4699}, new int[]{1387, 1393, 1401, 11998, 12000, 11787, 3053, 3054, 12795}),
	WATER_RUNE(555, new int[]{4694, 4695, 4698}, new int[]{1383, 1395, 1403, 20730, 20733, 6562, 6563, 11787, 11789, 12795}),
	EARTH_RUNE(557, new int[]{4698, 4696, 4699}, new int[]{3053, 3054, 1385, 1399, 1407, 6562, 6563, 20736, 20739}),
	MIND_RUNE(558, null, null),
	BODY_RUNE(559, null, null),
	DEATH_RUNE(560, null, null),
	NATURE_RUNE(561, null, null),
	CHAOS_RUNE(562, null, null),
	LAW_RUNE(563, null, null),
	COSMIC_RUNE(564, null, null),
	BLOOD_RUNE(565, null, null),
	SOUL_RUNE(566, null, null),
	ASTRAL_RUNE(9075, null, null);

	private final int mainId;
	private final int[] combos;
	private final int[] staffs;

	MagicRune(int mainId, int[] combos, int[] staffs) {
		this.mainId = mainId;
		this.combos = combos;
		this.staffs = staffs;
	}

	public int getMainId() {
		return mainId;
	}

	public int[] getCombos() {
		return combos;
	}

	public int[] getStaffs() {
		return staffs;
	}

	public static MagicRune forMainId(int id) {
		for (MagicRune rune : values()) {
			if (rune.mainId == id)
				return rune;
		}
		return null;
	}

	public boolean isStaff(int id) {
		if (staffs == null) return false;
		for (int staff : staffs) {
			if (staff == id) {
				return true;
			}
		}
		return false;
	}

	public boolean containsCombo(ItemContainer container, int amount) {
		if (combos == null) return false;
		for (int combo : combos) {
			if (container.contains(new Item(combo, amount))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasRunes(Player player, Item[] required) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);

		for (Item item : required) {
			MagicRune rune = forMainId(item.getId());

			if (rune == null)
				continue;

			if (weapon != null && rune.isStaff(weapon.getId()))
				continue;

			if (player.getInventory().contains(new Item(rune.mainId, item.getAmount())))
				continue;

			if (rune.containsCombo(player.getInventory(), item.getAmount()))
				continue;

			/* does not have runes */
			return false;
		}
		return true;
	}

	public static void remove(Player player, Item[] required) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);

		boolean refresh = false;
		player.getInventory().setFiringEvents(false);

		for (Item item : required) {
			MagicRune rune = forMainId(item.getId());

			if (rune == null)
				continue;

			if (weapon != null && rune.isStaff(weapon.getId()))
				continue;

			if (player.getInventory().contains(new Item(rune.mainId, item.getAmount()))) {
				refresh |= player.getInventory().remove(new Item(rune.mainId, item.getAmount())) > -1;
				continue;
			}

			if (rune.combos != null && rune.containsCombo(player.getInventory(), item.getAmount())) {
				for (int combo : rune.combos) {
					if (player.getInventory().contains(new Item(combo, item.getAmount()))) {
						refresh |= player.getInventory().remove(new Item(combo, item.getAmount())) > -1;
						break;
					}
				}
			}
		}

		player.getInventory().setFiringEvents(true);
		if (refresh) player.getInventory().refreshBulk(player, Inventory.INVENTORY_DISPLAY_ID);
	}

	public static boolean hasRunes(Player player, RequiredRune[] runes) {
		List<Item> required = new LinkedList<>();
		for (RequiredRune rune : runes) {
			required.add(new Item(rune.getMainId(), rune.getAmount()));
		}
		return hasRunes(player, required.toArray(new Item[required.size()]));
	}

	public static void remove(Player player, RequiredRune[] runes) {
		List<Item> required = new LinkedList<>();
		for (RequiredRune rune : runes) {
			required.add(new Item(rune.getMainId(), rune.getAmount()));
		}
		remove(player, required.toArray(new Item[required.size()]));
	}
	
}
