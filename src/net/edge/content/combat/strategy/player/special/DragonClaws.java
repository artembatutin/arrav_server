package net.edge.content.combat.strategy.player.special;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FormulaFactory;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael | Chex
 */
public class DragonClaws extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(10961, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1950, 50);

    @Override
    public void start(Player attacker, Actor defender) {
        super.start(attacker, defender);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void finish(Player attacker, Actor defender) {
        WeaponInterface.setStrategy(attacker);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        CombatHit first = nextMeleeHit(attacker, defender);
        if(!first.isAccurate()) {
            return secondOption(attacker, defender, first);
        }
        CombatHit second = first.copyAndModify(damage -> damage / 2);
        CombatHit third = second.copyAndModify(damage -> damage / 2);
        return new CombatHit[] { first, second, third, third };
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }

    private CombatHit[] secondOption(Player attacker, Actor defender, CombatHit inaccurate) {
        CombatHit second = nextMeleeHit(attacker, defender);
        if(!second.isAccurate()) {
            return thirdOption(attacker, defender, inaccurate, second);
        }
        CombatHit third = second.copyAndModify(damage -> damage / 2);
        return new CombatHit[] { inaccurate, second, third, third };
    }

    public CombatHit[] thirdOption(Player attacker, Actor defender, CombatHit inaccurate, CombatHit inaccurate2) {
        int hit = RandomUtils.inclusive(0, (int) (FormulaFactory.getMaxMeleeHit(attacker) * 0.75));
        CombatHit third = new CombatHit(new Hit(hit, HitIcon.MELEE), CombatUtil.getHitDelay(attacker, defender, getCombatType()), CombatUtil.getHitsplatDelay(getCombatType()));
        if(hit == 0) {
            return fourthOption(attacker, defender, inaccurate, inaccurate2, third);
        }
        return new CombatHit[]{inaccurate, inaccurate2, third, third};
    }

    public CombatHit[] fourthOption(Player attacker, Actor defender, CombatHit inaccurate, CombatHit inaccurate2, CombatHit inaccurate3) {
        int boost = nextMeleeHit(attacker, defender).getDamage() + RandomUtils.inclusive(0, (int) (FormulaFactory.getMaxMeleeHit(attacker) * 0.50));
        CombatHit fourth = new CombatHit(new Hit(boost, HitIcon.MELEE), CombatUtil.getHitDelay(attacker, defender, getCombatType()), CombatUtil.getHitsplatDelay(getCombatType()));
        if(!fourth.isAccurate()) {
            int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
            int hitSplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
            return new CombatHit[]{inaccurate, inaccurate2, new CombatHit(new Hit(1, HitIcon.MELEE), hitDelay, hitSplatDelay), new CombatHit(new Hit(1, HitIcon.MELEE), hitDelay, hitSplatDelay)};
        }
        return new CombatHit[] { inaccurate, inaccurate2, inaccurate3, fourth };
    }

}
