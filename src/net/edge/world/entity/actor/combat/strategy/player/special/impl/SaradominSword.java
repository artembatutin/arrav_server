package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.AttackModifier;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.formula.FormulaFactory;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class SaradominSword extends PlayerMeleeStrategy {
	
	private static final Graphic GRAPHIC = new Graphic(1194);
	private static final Animation ANIMATION = new Animation(11993, Animation.AnimationPriority.HIGH);
	private static final AttackModifier MODIFIER = new AttackModifier().accuracy(0.1);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		defender.graphic(GRAPHIC);
	}

	@Override
	public void finishOutgoing(Player attacker, Actor defender) {
		WeaponInterface.setStrategy(attacker);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Actor defender) {
		return new CombatHit[]{nextMeleeHit(attacker, defender), nextMagicHit(attacker, defender, FormulaFactory.maxHit(attacker, defender, CombatType.MELEE), 1, 0)};
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
