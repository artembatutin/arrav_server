package net.edge.content.newcombat.weapon;

import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.content.RangedAmmunition;
import net.edge.world.entity.item.container.impl.Equipment;

public class RangedWeaponDefinition {
    private final int level;
    private final AttackType type;
    private final RangedAmmunition[] allowed;

    public RangedWeaponDefinition(int level, AttackType type, RangedAmmunition[] allowed) {
        this.level = level;
        this.type = type;
        this.allowed = allowed;
    }

    public int getLevel() {
        return level;
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
    }
}