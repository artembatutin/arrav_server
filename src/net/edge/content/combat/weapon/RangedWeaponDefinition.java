package net.edge.content.combat.weapon;

import net.edge.world.entity.item.container.impl.Equipment;

public class RangedWeaponDefinition {
	private final AttackType type;
	private final RangedAmmunition[] allowed;

	public RangedWeaponDefinition(AttackType type, RangedAmmunition[] allowed) {
		this.type = type;
		this.allowed = allowed;
	}

	public int getSlot() {
		return type.slot;
	}

	public RangedAmmunition[] getAllowed() {
		return allowed;
	}

	public AttackType getType() {
		return type;
	}

	public enum AttackType {
		SHOT(Equipment.ARROWS_SLOT),
		THROWN(Equipment.WEAPON_SLOT);

		final int slot;

		AttackType(int slot) {
			this.slot = slot;
		}

		public int getSlot() {
			return slot;
		}
	}
}