package com.rageps.world.entity.item.container.impl;

/**
 * An enumeration of equipment types.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public enum EquipmentType {
	
	AMULET(Equipment.AMULET_SLOT),
	ARROWS(Equipment.ARROWS_SLOT),
	BODY(Equipment.CHEST_SLOT),
	BOOTS(Equipment.FEET_SLOT),
	CAPE(Equipment.CAPE_SLOT),
	FULL_HELMET(Equipment.HEAD_SLOT),
	FULL_MASK(Equipment.HEAD_SLOT),
	HAT(Equipment.HEAD_SLOT),
	GLOVES(Equipment.HANDS_SLOT),
	LEGS(Equipment.LEGS_SLOT),
	PLATEBODY(Equipment.CHEST_SLOT),
	RING(Equipment.RING_SLOT),
	SHIELD(Equipment.SHIELD_SLOT),
	WEAPON(Equipment.WEAPON_SLOT);
	
	/**
	 * The slot of the equipment type.
	 */
	private final int slot;
	
	/**
	 * Constructs a single {@link EquipmentType}.
	 * @param slot the slot of the equipment type.
	 */
	EquipmentType(int slot) {
		this.slot = slot;
	}
	
	/**
	 * Gets the equipment slot.
	 * @return equipment slot.
	 */
	public int getSlot() {
		return slot;
	}
}
