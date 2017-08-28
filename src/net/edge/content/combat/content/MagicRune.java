package net.edge.content.combat.content;

import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.ItemContainer;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.world.entity.item.container.impl.Inventory;

import java.util.*;

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

	public static boolean hasRunes(Player player, RequiredRune[] runes) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		Map<MagicRune, Integer> amounts = new HashMap<>();
		ItemContainer inventory = player.getInventory();
		inventory = new ItemContainer(inventory.capacity(), inventory.policy(), Arrays.copyOf(inventory.getItems(), inventory.getItems().length));

		for(RequiredRune rune : runes) {
			amounts.put(rune.getRune(), rune.getAmount());
		}

		for(RequiredRune rune : runes) {
			int remaining = amounts.get(rune.getRune());
			if(remaining <= 0) continue;

			if(weapon != null && rune.getStaffs() != null) {
				for(int id : rune.getStaffs()) {
					if(weapon.getId() == id) {
						amounts.replace(rune.getRune(), remaining = 0);
					}
				}
			}

			if(remaining > 0) {
				if(rune.getCombos() != null && inventory.containsAny(rune.getCombos())) {
					for(int id : rune.getCombos()) {
						int slot = inventory.computeIndexForId(id);
						if(slot != -1) {
							int amount = inventory.get(slot).getAmount();
							if(amount > remaining) amount = remaining;
							inventory.remove(new Item(id, amount), slot, false);

							for(MagicRune combo : combos(id)) {
								if(amounts.containsKey(combo)) amounts.put(combo, amounts.get(combo) - amount);
							}

							if(remaining - amount <= 0) break;
						}
					}
				} else {
					int slot = inventory.computeIndexForId(rune.getMainId());
					if(slot != -1) {
						int amount = inventory.get(slot).getAmount();
						if(amount > remaining) amount = remaining;
						inventory.remove(new Item(rune.getMainId(), amount), slot, false);
						amounts.replace(rune.getRune(), remaining - amount);
					}
				}
			}
		}

		for(RequiredRune rune : runes) {
			if(amounts.get(rune.getRune()) > 0) return false;
		}

		return true;
	}

	private static MagicRune[] combos(int id) {
		Set<MagicRune> runes = new HashSet<>();
		for(MagicRune rune : values()) {
			if(rune.combos == null) continue;
			for(int combo : rune.combos) {
				if(combo == id) runes.add(rune);
			}
		}
		return runes.toArray(new MagicRune[0]);
	}

	public static void remove(Player player, RequiredRune[] runes) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		Map<MagicRune, Integer> amounts = new HashMap<>();

		for(RequiredRune rune : runes) {
			int remaining = rune.getAmount();
			if(weapon != null && rune.getStaffs() != null) {
				for(int id : rune.getStaffs()) {
					if(weapon.getId() == id) remaining = 0;
				}
			}
			amounts.put(rune.getRune(), remaining);
		}

		for(RequiredRune rune : runes) {
			int remaining = amounts.get(rune.getRune());
			if(remaining > 0) {
				if(rune.getCombos() != null && player.getInventory().containsAny(rune.getCombos())) {
					for(int id : rune.getCombos()) {
						int slot = player.getInventory().computeIndexForId(id);
						if(slot != -1) {
							int amount = player.getInventory().get(slot).getAmount();
							if(amount > remaining) amount = remaining;
							player.getInventory().remove(new Item(id, amount), slot, false);

							for(MagicRune combo : combos(id)) {
								if(amounts.containsKey(combo)) amounts.put(combo, amounts.get(combo) - amount);
							}

							if(remaining - amount <= 0) break;
						}
					}
				} else {
					int slot = player.getInventory().computeIndexForId(rune.getMainId());
					if(slot != -1) {
						int amount = player.getInventory().get(slot).getAmount();
						if(amount > remaining) amount = remaining;
						player.getInventory().remove(new Item(rune.getMainId(), amount), slot, false);
						amounts.replace(rune.getRune(), remaining - amount);
					}
				}
			}
		}
		player.getInventory().refreshBulk(player, Inventory.INVENTORY_DISPLAY_ID);
	}

}
