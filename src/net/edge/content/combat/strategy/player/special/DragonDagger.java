package net.edge.content.combat.strategy.player.special;

import net.edge.content.achievements.Achievement;
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
public class DragonDagger extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(1062, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(252, 100);

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        super.attack(attacker, defender, hit);
        attacker.graphic(GRAPHIC);
        attacker.getCombatSpecial().drain(attacker);
    }

    @Override
    public void finish(Player attacker, Actor defender) {
        Achievement.DRAGON_DAGGER.inc(attacker);
        WeaponInterface.setStrategy(attacker);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(attacker, defender);
        return new CombatHit[] { nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay), nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay) };
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
