package net.edge.world.entity.actor.combat.weapon;

import net.edge.world.entity.item.container.impl.Equipment;

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
