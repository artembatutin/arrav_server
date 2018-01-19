package net.arrav.world.entity.actor.combat.strategy.player.special.impl;

import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.formula.FormulaFactory;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.arrav.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class SaradominSword extends PlayerMeleeStrategy {

    private static final Graphic GRAPHIC = new Graphic(1194);
    private static final Animation ANIMATION = new Animation(11993, Animation.AnimationPriority.HIGH);

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        defender.graphic(GRAPHIC);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender), nextMagicHit(attacker, defender, FormulaFactory.getMaxHit(attacker, defender, CombatType.MELEE), 1, 0) };
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        return 4;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }

    @Override
    public int modifyAccuracy(Player attacker, Actor defender, int roll) {
        return (int) (roll * 1.10);
    }

}
