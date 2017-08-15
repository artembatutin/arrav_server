package net.edge.content.newcombat.strategy.npc;

import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.strategy.basic.RangedStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcRangedStrategy extends RangedStrategy<Mob> {

    private final CombatProjectileDefinition projectileDefinition;

    public NpcRangedStrategy(CombatProjectileDefinition projectileDefinition) {
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return true;
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return 4;
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
        return 10;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMagicHit(attacker, defender, projectileDefinition.getMaxHit(), projectileDefinition.getHitDelay(), projectileDefinition.getHitsplatDelay()) };
    }

}
