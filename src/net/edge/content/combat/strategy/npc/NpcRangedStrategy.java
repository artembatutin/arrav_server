package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.basic.RangedStrategy;
import net.edge.world.Animation;
import net.edge.world.PoisonType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcRangedStrategy extends RangedStrategy<Mob> {

    private final CombatProjectileDefinition projectileDefinition;

    public NpcRangedStrategy(CombatProjectileDefinition projectileDefinition) {
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public void hit(Mob attacker, Actor defender, Hit hit) {
        if (attacker.getDefinition().poisonous()) {
            defender.poison(CombatPoisonEffect.getPoisonType(attacker.getId()).orElse(PoisonType.DEFAULT_NPC));
        }
    }

    @Override
    public int getAttackDelay(Mob attacker, FightType fightType) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
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
