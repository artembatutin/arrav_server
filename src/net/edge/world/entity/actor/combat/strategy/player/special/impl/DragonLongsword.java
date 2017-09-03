package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.content.skill.prayer.Prayer;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.AttackModifier;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public final class DragonLongsword extends PlayerMeleeStrategy {

    private static final Animation ANIMATION = new Animation(1058, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(248, 100);
    private static final AttackModifier MODIFIER = new AttackModifier().damage(0.15);

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void finishOutgoing(Player attacker, Actor defender) {
        WeaponInterface.setStrategy(attacker);
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
    public Optional<AttackModifier> getModifier(Player attacker) {
        return Optional.of(MODIFIER);
    }
}
