package net.edge.world.entity.actor.combat.strategy.player.special;

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

/** @author Michael | Chex */
public class ArmadylGodsword extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(11989, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(2113);
	private static final AttackModifier MODIFIER = new AttackModifier().accuracy(1.00).damage(0.375);

	@Override
	public void finishOutgoing(Player attacker, Actor defender) {
		super.finishOutgoing(attacker, defender);
		attacker.graphic(GRAPHIC);
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
