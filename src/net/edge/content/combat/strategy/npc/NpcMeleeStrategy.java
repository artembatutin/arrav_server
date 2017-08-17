package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.MeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.PoisonType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcMeleeStrategy extends MeleeStrategy<Mob> {

    @Override
    public void hit(Mob attacker, Actor defender, Hit hit, Hit[] hits) {
        if (attacker.getDefinition().poisonous()) {
            defender.poison(CombatPoisonEffect.getPoisonType(attacker.getId()).orElse(PoisonType.DEFAULT_NPC));
        }
    }

    @Override
    public void block(Actor attacker, Mob defender, Hit hit, Hit[] hits) {
        defender.animation(getBlockAnimation(defender, attacker));
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 1;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender, 1, 0) };
    }

    @Override
    protected Animation getAttackAnimation(Mob attacker, Actor defender) {
        return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    protected Animation getBlockAnimation(Mob defender, Actor attacker) {
        return new Animation(defender.getDefinition().getDefenceAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
