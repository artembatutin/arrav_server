package net.edge.content.combat.strategy.npc.boss.kbd;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.npc.NpcMagicStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class KBDPoisonStrategy extends NpcMagicStrategy {
    private static final Animation ANIMATION = new Animation(81, Animation.AnimationPriority.HIGH);

    KBDPoisonStrategy() {
        super(CombatProjectileDefinition.getDefinition("KBD poison"));
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return 5;
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 10;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 100) };
    }

    @Override
    public Animation getAttackAnimation(Mob attacker, Actor defender) {
        return ANIMATION;
    }
}
