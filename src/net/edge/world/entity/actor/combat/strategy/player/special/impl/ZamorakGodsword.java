package net.edge.world.entity.actor.combat.strategy.player.special.impl;

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
public final class ZamorakGodsword extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(7070, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1221);

	private static final AttackModifier MODIFIER = new AttackModifier().accuracy(0.1).damage(0.4);

	@Override
	public void attack(Player attacker, Actor defender, Hit h) {
		super.attack(attacker, defender, h);
		if(h.isAccurate()) {
			attacker.graphic(GRAPHIC);
			defender.graphic(new Graphic(2104));
			if(!defender.isFrozen() && defender.size() == 1) {
				defender.freeze(20);
			}
		} else {
			defender.graphic(new Graphic(339, 10));
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
	public Optional<AttackModifier> getModifier(Player attacker) {
		return Optional.of(MODIFIER);
	}

}
