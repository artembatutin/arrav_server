package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.CombatModifier;
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
public final class Anchor extends PlayerMeleeStrategy {

    private static final Animation ANIMATION = new Animation(5870, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1027, 50);
    private static final CombatModifier MODIFIER = new CombatModifier().attack(0.3).damage(0.1);

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void attack(Player player, Actor target, Hit hit) {
        super.attack(player, target, hit);

        if(target.isPlayer() && hit.isAccurate()) {
            //Finalizing with the anchor effect. (decreasing random combat skill).
            int skill = RandomUtils.random(Skills.DEFENCE, Skills.ATTACK, Skills.MAGIC, Skills.RANGED);
            double decreaseValue = (hit.getDamage() / 100.0) * 10.0;
            Player victim = target.toPlayer();
            victim.getSkills()[skill].decreaseLevel((int) decreaseValue);
            Skills.refresh(victim, skill);
        }
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
    public Optional<CombatModifier> getModifier(Player attacker) {
        return Optional.of(MODIFIER);
    }
}
