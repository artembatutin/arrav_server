package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.AttackModifier;
import net.edge.world.entity.actor.combat.formula.FormulaFactory;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class DragonClaws extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(10961, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1950, 50);
	private static final AttackModifier MODIFIER = new AttackModifier().accuracy(0.75).damage(0.05);

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
	public CombatHit[] getHits(Player attacker, Actor defender) {
		CombatHit first = nextMeleeHit(attacker, defender);

		if(first.getDamage() < 1) {
			return secondOption(attacker, defender, first);
		}

		CombatHit second = first.copyAndModify(damage -> damage / 2);
		CombatHit third = second.copyAndModify(damage -> damage / 2);
		CombatHit fourth = second.copyAndModify(damage -> first.getDamage() - second.getDamage() - third.getDamage());
		return new CombatHit[]{first, second, third, fourth};
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Actor defender) {
		return ANIMATION;
	}

	private CombatHit[] secondOption(Player attacker, Actor defender, CombatHit inaccurate) {
		CombatHit second = nextMeleeHit(attacker, defender);

		if(second.getDamage() < 1) {
			return thirdOption(attacker, defender, inaccurate, second);
		}

		CombatHit third = second.copyAndModify(damage -> damage / 2);
		return new CombatHit[]{inaccurate, second, third, third};
	}

	private CombatHit[] thirdOption(Player attacker, Actor defender, CombatHit inaccurate, CombatHit inaccurate2) {
		CombatHit third = nextMeleeHit(attacker, defender);

		if(third.getDamage() < 1) {
			return fourthOption(attacker, defender, inaccurate, inaccurate2);
		}

		int maxHit = FormulaFactory.maxHit(attacker, defender, CombatType.MELEE) * 3 / 4;
		third.setDamage(maxHit);
		CombatHit fourth = third.copyAndModify(damage -> maxHit);
		return new CombatHit[]{inaccurate, inaccurate2, third, fourth};
	}

	private CombatHit[] fourthOption(Player attacker, Actor defender, CombatHit inaccurate, CombatHit inaccurate2) {
		CombatHit fourth = nextMeleeHit(attacker, defender);

		if(fourth.getDamage() < 1) {
			int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
			int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
			CombatHit hit = new CombatHit(new Hit(10, HitIcon.MELEE), hitDelay, hitsplatDelay);
			return new CombatHit[]{inaccurate, inaccurate2, hit, hit};
		}

		fourth.modifyDamage(damage -> (int) (damage * 1.50));
		return new CombatHit[]{inaccurate, inaccurate2, fourth, fourth};
	}

	@Override
	public Optional<AttackModifier> getModifier(Player attacker) {
		return Optional.of(MODIFIER);
	}
}
