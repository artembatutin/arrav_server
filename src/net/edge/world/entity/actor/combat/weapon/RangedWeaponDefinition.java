package net.edge.world.entity.actor.combat.weapon;

public class RangedWeaponDefinition {
	
	private final RangedWeaponType type;
	private final RangedAmmunition[] allowed;
	
	public RangedWeaponDefinition(RangedWeaponType type, RangedAmmunition[] allowed) {
		this.type = type;
		this.allowed = allowed;
	}
	
	public int getSlot() {
		return type.slot;
	}
	
	public RangedAmmunition[] getAllowed() {
		return allowed;
	}
	
	public RangedWeaponType getType() {
		return type;
	}
	
}