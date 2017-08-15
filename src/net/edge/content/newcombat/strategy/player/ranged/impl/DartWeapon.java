package net.edge.content.newcombat.strategy.player.ranged.impl;

import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.strategy.player.ranged.ThrownWeapon;
import net.edge.content.newcombat.weapon.RangedWeaponDefinition;

public class DartWeapon extends ThrownWeapon {

    public DartWeapon(RangedWeaponDefinition definition, CombatProjectileDefinition projectileDefinition) {
        super(definition, projectileDefinition);
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return stance == AttackStance.RAPID ? 2 : 3;
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
        return stance == AttackStance.LONGRANGE ? 5 : 3;
    }

    @Override
    protected String getLastThrownMessage() {
        return "You threw your last dart!";
    }

}
