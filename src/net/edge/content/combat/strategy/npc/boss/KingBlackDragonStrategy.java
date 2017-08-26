package net.edge.content.combat.strategy.npc.boss;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.npc.MultiStrategy;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.content.combat.strategy.npc.impl.DragonfireStrategy;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

/** @author Michael | Chex */
public class KingBlackDragonStrategy extends MultiStrategy {
    private static final Melee MELEE = new Melee();
    private static final Dragonfire DRAGONFIRE = new Dragonfire();
    private static final Poison POISON = new Poison();
    private static final Freeze FREEZE = new Freeze();
    private static final Shock SHOCK = new Shock();

    private static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(MELEE, DRAGONFIRE, POISON, FREEZE, SHOCK);
    private static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(DRAGONFIRE, POISON, FREEZE, SHOCK);

    public KingBlackDragonStrategy() {
        currentStrategy = randomStrategy(NON_MELEE);
    }

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);
    }

    @Override
    public void finish(Mob attacker, Actor defender) {
        currentStrategy.finish(attacker, defender);
        if (MELEE.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        } else {
            currentStrategy = randomStrategy(NON_MELEE);
        }
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return attacker.getDefinition().getAttackDelay();
    }

    private static final class Melee extends NpcMeleeStrategy {
        private static final Animation CRUSH = new Animation(80, Animation.AnimationPriority.HIGH);
        private static final Animation STAB = new Animation(91, Animation.AnimationPriority.HIGH);
        private static final Animation[] ANIMS = { CRUSH, STAB };

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 1;
        }

        @Override
        public Animation getAttackAnimation(Mob attacker, Actor defender) {
            return RandomUtils.random(ANIMS);
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }


    private static final class Dragonfire extends DragonfireStrategy {

        Dragonfire() {
            super(CombatProjectileDefinition.getDefinition("KBD fire"));
        }

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
        }

    }

    private static final class Freeze extends DragonfireStrategy {

        Freeze() {
            super(CombatProjectileDefinition.getDefinition("KBD freeze"));
        }

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
        }

    }

    private static final class Shock extends DragonfireStrategy {

        Shock() {
            super(CombatProjectileDefinition.getDefinition("KBD shock"));
        }

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
        }

    }

    private static final class Poison extends DragonfireStrategy {

        Poison() {
            super(CombatProjectileDefinition.getDefinition("KBD poison"));
        }

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
        }
    }

}
