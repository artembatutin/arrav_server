package net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.bandos;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Animation;
import net.arrav.world.World;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.strategy.CombatStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.MultiStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.boss.KingBlackDragonStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.impl.godwars.GeneralGraardor;
import net.arrav.world.entity.actor.player.Player;
import org.w3c.dom.ranges.Range;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class GeneralGraardorStrategy extends MultiStrategy {

    private static final Melee MELEE = new Melee();
    private static final Smash SMASH = new Smash();

    public GeneralGraardorStrategy() {
            currentStrategy = MELEE;
        }

        @Override
        public boolean canAttack(Mob attacker, Actor defender) {
            return defender.isPlayer() && GeneralGraardor.CHAMBER.inLocation(defender.getPosition());

        }

        @Override
        public void start(Mob attacker, Actor defender, Hit[] hits) {
            attacker.forceChat(RandomUtils.random(GeneralGraardor.RANDOM_CHAT));
            if(RandomUtils.inclusive(100) > 30) {
                currentStrategy = MELEE;
            } else {
                currentStrategy = SMASH;
            }
            currentStrategy.start(attacker, defender, hits);
        }

        @Override
        public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
            currentStrategy.block(attacker, defender, hit, combatType);
            defender.getCombat().attack(attacker);
        }

        @Override
        public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
            return attacker.getDefinition().getAttackDelay();
        }

        @Override
        public boolean hitBack() {
            return true;
        }

    /**
     * Represents the melee attack style of {@link GeneralGraardorStrategy}
     */
    private static final class Melee extends NpcMeleeStrategy {

        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 1;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }

    /**
     * Represents the Melee Smash attack style of {@link GeneralGraardorStrategy}
     */
    private static final class Smash extends NpcMeleeStrategy {


        @Override
        public int getAttackDistance(Mob attacker, FightType fightType) {
            return 1;
        }

        private static final Animation ANIMATION = new Animation(7063, Animation.AnimationPriority.HIGH);

        @Override
        public Animation getAttackAnimation(Mob attacker, Actor defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Mob attacker, Actor defender) {
            ObjectList<Player> toHit = new ObjectArrayList<>();
            World.get().getLocalPlayers(defender).forEachRemaining(player -> {
                if(GeneralGraardor.CHAMBER.inLocation(player.getPosition())) {
                    toHit.add(player);
                }
            });
            toHit.forEach(player -> nextMeleeHit(attacker, player, 300));
            return new CombatHit[]{nextMeleeHit(attacker, defender, 300)};
        }
        }
    }