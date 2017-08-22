package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.MeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcMeleeStrategy extends MeleeStrategy<Mob> {

    public static final NpcMeleeStrategy INSTANCE = new NpcMeleeStrategy();

    private NpcMeleeStrategy() { }

    @Override
    public void attack(Mob attacker, Actor defender, Hit hit) {
        attacker.animation(getAttackAnimation(attacker, defender));
    }

    @Override
    public void hit(Mob attacker, Actor defender, Hit hit) {
        CombatPoisonEffect.getPoisonType(attacker.getId()).ifPresent(p -> {
            if(hit.isAccurate() && attacker.getDefinition().poisonous()) {
                defender.poison(p);
            }
        });
    }

    @Override
    public int getAttackDelay(Mob attacker, FightType fightType) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 1;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender) };
    }

    @Override
    public Animation getAttackAnimation(Mob attacker, Actor defender) {
        return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
