package net.edge.content.combat.strategy.npc.boss.kbd;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.npc.boss.BossStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class KingBlackDragon extends BossStrategy {
    private static final KBDMeleeStrategy MELEE = new KBDMeleeStrategy();
    private static final KBDDragonFireStrategy DRAGONFIRE = new KBDDragonFireStrategy();
    private static final KBDPoisonStrategy POISON = new KBDPoisonStrategy();
    private static final KBDFreezeStrategy FREEZE = new KBDFreezeStrategy();
    private static final KBDShockStrategy SHOCK = new KBDShockStrategy();

    private static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(MELEE, DRAGONFIRE, POISON, FREEZE, SHOCK);
    private static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(DRAGONFIRE, POISON, FREEZE, SHOCK);

    public KingBlackDragon() {
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

}
