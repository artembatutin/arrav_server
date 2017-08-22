package net.edge.content.combat.strategy.player.special;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * @author Michael | Chex
 */
public class DragonClaws extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(10961, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1950, 50);

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        super.attack(attacker, defender, hit);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void finish(Player attacker, Actor defender) {
        WeaponInterface.setStrategy(attacker);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(attacker, defender);
        return new CombatHit[] {//FIXME
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),

            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay),
            nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay)
        };
    }

    @Override
    public int getAttackDelay(Player attacker, FightType fightType) {
        return 4;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }
}
