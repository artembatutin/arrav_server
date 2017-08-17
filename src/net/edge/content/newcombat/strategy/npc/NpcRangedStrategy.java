package net.edge.content.newcombat.strategy.npc;

import net.edge.content.combat.weapon.FightType;
import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.CombatType;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.strategy.basic.RangedStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcRangedStrategy extends RangedStrategy<Mob> {

    private final CombatProjectileDefinition projectileDefinition;

    public NpcRangedStrategy(CombatProjectileDefinition projectileDefinition) {
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public int getAttackDelay(FightType fightType) {
        return 4;
    }

    @Override
    public int getAttackDistance(FightType fightType) {
        return 10;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMagicHit(attacker, defender, projectileDefinition.getMaxHit(), projectileDefinition.getHitDelay(attacker, defender, false), projectileDefinition.getHitsplatDelay()) };
    }

    @Override
    protected Animation getAttackAnimation(Mob attacker, Actor defender) {
        return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    protected Animation getBlockAnimation(Mob attacker, Actor defender) {
        return new Animation(attacker.getDefinition().getDefenceAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }
}
