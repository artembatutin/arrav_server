package net.edge.content.combat.strategy.npc.impl;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.npc.MultiStrategy;
import net.edge.content.combat.strategy.npc.NpcMagicStrategy;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

/** @author Michael | Chex */
public class Dragon extends MultiStrategy {
    private static final DragonFireStrategy DRAGONFIRE = new DragonFireStrategy();
    private static final CombatStrategy<Mob>[] STRATEGIES = createStrategyArray(NpcMeleeStrategy.INSTANCE, DRAGONFIRE);

    public Dragon() {
        currentStrategy = NpcMeleeStrategy.INSTANCE;
    }

    @Override
    public void finish(Mob attacker, Actor defender) {
        currentStrategy.finish(attacker, defender);
        currentStrategy = randomStrategy(STRATEGIES);
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return attacker.getDefinition().getAttackDelay();
    }

    private static final class DragonFireStrategy extends NpcMagicStrategy {
        private static final Animation ANIMATION = new Animation(81, Animation.AnimationPriority.HIGH);

        DragonFireStrategy() {
            super(CombatProjectileDefinition.getDefinition("Dragonfire"));
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
            return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 600, true) };
        }

    }

}
