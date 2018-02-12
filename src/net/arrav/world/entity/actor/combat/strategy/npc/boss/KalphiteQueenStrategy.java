package net.arrav.world.entity.actor.combat.strategy.npc.boss;

import net.arrav.world.Animation;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.strategy.CombatStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.MultiStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMagicStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.armadyl.KreeArraStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.bandos.GeneralGraardorStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.impl.KalphiteQueen;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Dave/Ophion
 * Date: 05/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class KalphiteQueenStrategy extends MultiStrategy {

    public KalphiteQueenStrategy() {
        currentStrategy = randomStrategy(FULL_STRATEGIES);
    }

    private static final Melee MELEE = new Melee();
    private static final Ranged RANGED = new Ranged();
    private static final Magic MAGIC = new Magic();

    private static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(MELEE, MAGIC, RANGED);
    private static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(MAGIC, RANGED);

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return defender.isPlayer();
    }

    @Override
    public void start(Mob attacker, Actor defender, Hit[] hits) {
        if (MELEE.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        } else {
            currentStrategy = randomStrategy(NON_MELEE);
        }
        currentStrategy.start(attacker, defender, hits);
    }

    @Override
    public boolean hitBack() {
        return true;
    }

    @Override
    public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
        switch(attacker.getStrategy().getCombatType()) {
            case MELEE:
               // if(kalphiteQueen.getPhase().equals(KalphiteQueen.Phase.PHASE_TWO)) {
                    Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).filter(Objects::nonNull).forEach(h -> h.modifyDamage(damage -> h.getDamage() / 2));
               // }
                break;
            case RANGED:
                //if(kalphiteQueen.getPhase().equals(KalphiteQueen.Phase.PHASE_ONE)) {
                    Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).filter(Objects::nonNull).forEach(h -> h.modifyDamage(damage -> h.getDamage() / 2));

                //}
                break;
            case MAGIC:
               // if(kalphiteQueen.getPhase().equals(KalphiteQueen.Phase.PHASE_ONE)) {
                    Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).filter(Objects::nonNull).forEach(h -> h.modifyDamage(damage -> h.getDamage() / 2));

                //}
                break;
        }
        currentStrategy.block(attacker, defender, hit, combatType);
    }

    /**
     * Represents the melee attack style of {@link KalphiteQueenStrategy}
     */
    private static final class Melee extends NpcMeleeStrategy {

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 2;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }
    /**
     * Represents the melee attack style of {@link KalphiteQueenStrategy}
     */
    private static final class Ranged extends NpcRangedStrategy {

        private Ranged() {
            super(CombatProjectile.getDefinition("Kalphite Ranged"));
        }

        KalphiteQueen.Phase phase;

//        @Override
//        public Animation getAttackAnimation(Mob attacker, Actor defender) {
//            defender.toPlayer().message("phase: " + phase);
//            return new Animation(phase.equals(KalphiteQueen.Phase.PHASE_ONE) ? 6420 : 6234);
//        }

    }
    /**
     * Represents the melee attack style of {@link KalphiteQueenStrategy}
     */
    private static final class Magic extends NpcMagicStrategy {

        private Magic() {
            super(CombatProjectile.getDefinition("Kalphite Magic"));
        }
        KalphiteQueen.Phase phase;

//        @Override
//        public Animation getAttackAnimation(Mob attacker, Actor defender) {
//            defender.toPlayer().message("phase: " + phase);
//            return new Animation(phase.equals(KalphiteQueen.Phase.PHASE_ONE) ? 6420 : 6234);
//        }


    }

}
