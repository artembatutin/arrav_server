package net.edge.world.entity.actor.combat.strategy.player.special;

import net.edge.content.achievements.Achievement;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.AttackModifier;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author Michael | Chex
 */
public class DragonDagger extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1062, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(252, 100);

	private static final AttackModifier MODIFIER = new AttackModifier().accuracy(0.25).damage(0.15);

	@Override
	public void attack(Player attacker, Actor defender, Hit hit) {
		super.attack(attacker, defender, hit);
		attacker.graphic(GRAPHIC);
	}
	@Override
	public void finishOutgoing(Player attacker, Actor defender) {
		Achievement.DRAGON_DAGGER.inc(attacker);
		WeaponInterface.setStrategy(attacker);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Actor defender) {
		return new CombatHit[]{nextMeleeHit(attacker, defender), nextMeleeHit(attacker, defender)};
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
