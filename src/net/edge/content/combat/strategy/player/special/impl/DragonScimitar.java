package net.edge.content.combat.strategy.player.special.impl;

import net.edge.content.achievements.Achievement;
import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 28-8-2017.
 */
public class DragonScimitar extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(12031, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(2118, 100);
    private static final AttackModifier MODIFIER = new AttackModifier().accuracy(0.25);

    private static final Prayer[] PROTECTION_PRAYERS = new Prayer[]{Prayer.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES};

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        super.attack(attacker, defender, hit);
        attacker.graphic(GRAPHIC);

        if(hit.isAccurate() && defender.isPlayer()) {
            Player player = defender.toPlayer();
            Prayer.forActivated(player, PROTECTION_PRAYERS, p -> p.deactivate(player));
        }
    }

    @Override
    public void finish(Player attacker, Actor defender) {
        WeaponInterface.setStrategy(attacker);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender) };
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
