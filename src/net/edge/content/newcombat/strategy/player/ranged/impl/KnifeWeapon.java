package net.edge.content.newcombat.strategy.player.ranged.impl;

import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.strategy.player.ranged.ThrownWeapon;
import net.edge.content.newcombat.weapon.RangedWeaponDefinition;

public final class KnifeWeapon extends ThrownWeapon {

    public KnifeWeapon(RangedWeaponDefinition definition, CombatProjectileDefinition projectileDefinition) {
        super(definition, projectileDefinition);
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return stance.equals(AttackStance.RAPID) ? 2 : 3;
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
        return stance.equals(AttackStance.LONGRANGE) ? 6 : 4;
    }

    @Override
    protected String getLastThrownMessage() {
        return "You threw your last knife!";
    }

}
