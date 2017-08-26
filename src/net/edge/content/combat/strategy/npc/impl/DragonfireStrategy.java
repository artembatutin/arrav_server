package net.edge.content.combat.strategy.npc.impl;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.npc.NpcMagicStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class DragonfireStrategy extends NpcMagicStrategy {
    private static final Animation ANIMATION = new Animation(81, Animation.AnimationPriority.HIGH);

    public DragonfireStrategy(CombatProjectileDefinition projectileDefinition) {
        super(projectileDefinition);
    }

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
        return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 600, true)};
    }

}