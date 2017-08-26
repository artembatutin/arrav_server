package net.edge.content.combat.strategy.npc.boss.kbd;

import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class KBDMeleeStrategy extends NpcMeleeStrategy {

    private static final Animation ANIMATION = new Animation(80, Animation.AnimationPriority.HIGH);

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 1;
    }

    @Override
    public Animation getAttackAnimation(Mob attacker, Actor defender) {
        return ANIMATION;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender) };
    }
}
