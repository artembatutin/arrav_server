package net.edge.content.combat.strategy.player.special;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.hit.CombatHit;
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
        CombatHit second = first.copyAndModify(damage -> damage / 2);
        CombatHit third = second.copyAndModify(damage -> damage / 2);
        return new CombatHit[] { first, second, third, third };
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }

}
