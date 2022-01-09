package com.rageps.world.entity.actor.combat.ranged;

import com.rageps.world.entity.item.container.impl.Equipment;

public enum RangedWeaponType {
	SHOT(Equipment.ARROWS_SLOT), THROWN(Equipment.WEAPON_SLOT);
	
	final int slot;
	
	RangedWeaponType(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
}
