package net.arrav.world.entity.actor.combat.ranged;

import net.arrav.world.entity.item.container.impl.Equipment;

public enum RangedWeaponType {
	SHOT(Equipment.ARROWS_SLOT),
	THROWN(Equipment.WEAPON_SLOT);
	
	final int slot;
	
	RangedWeaponType(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
}
