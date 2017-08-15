package net.edge.content.newcombat.strategy.player.melee;

import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.MeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

public class FistStrategy extends MeleeStrategy<Player> {
    private static final Animation ACC_DEF = new Animation(422, Animation.AnimationPriority.HIGH);
    private static final Animation DEFAULT = new Animation(423, Animation.AnimationPriority.HIGH);

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        attacker.animation(getAttackAnimation(attacker));
        addCombatExperience(attacker, hit);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[]{nextMeleeHit(attacker, defender)};
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return 4;
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
        return 1;
    }

    private static Animation getAttackAnimation(Player player) {
        AttackStance stance = player.getNewCombat().getAttackStance();

        switch (stance) {
            case ACCURATE:
            case DEFENSIVE:
                return ACC_DEF;
        }
        return DEFAULT;
    }

}
