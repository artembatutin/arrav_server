package net.edge.content.newcombat.strategy.npc;

import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.strategy.basic.MeleeStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcMeleeStrategy extends MeleeStrategy<Mob> {

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
        return 1;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender, 1, 0) };
    }

}
