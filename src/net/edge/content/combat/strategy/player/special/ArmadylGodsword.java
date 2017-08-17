package net.edge.content.combat.strategy.player.special;

import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * @author Michael | Chex
 */
public class ArmadylGodsword extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(11989, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(2113);

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        super.attack(attacker, defender, hit, hits);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }
}
