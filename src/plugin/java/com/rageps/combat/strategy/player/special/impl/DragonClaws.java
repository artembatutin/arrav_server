package com.rageps.combat.strategy.player.special.impl;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.formula.FormulaFactory;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.combat.strategy.player.PlayerMeleeStrategy;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class DragonClaws extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(10961, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1950, 50);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Actor defender) {
		CombatHit first = nextMeleeHit(attacker, defender);

		if(first.getDamage() < 1) {
			return secondOption(attacker, defender, first);
		}

		CombatHit second = first.copyAndModify(defender, CombatType.MELEE, damage -> damage / 2);
		CombatHit third = second.copyAndModify(defender, CombatType.MELEE, damage -> damage / 2);
		CombatHit fourth = first.copyAndModify(defender, CombatType.MELEE, damage -> first.getDamage() - second.getDamage() - third.getDamage());
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

		CombatHit third = second.copyAndModify(defender, CombatType.MELEE, damage -> damage / 2);
		return new CombatHit[]{inaccurate, second, third, third};
	}

	private CombatHit[] thirdOption(Player attacker, Actor defender, CombatHit inaccurate, CombatHit inaccurate2) {
		CombatHit third = nextMeleeHit(attacker, defender);

		if(third.getDamage() < 1) {
			return fourthOption(attacker, defender, inaccurate, inaccurate2);
		}

		int maxHit = FormulaFactory.getMaxHit(attacker, defender, CombatType.MELEE) * 3 / 4;
		third.setDamage(maxHit);
		CombatHit fourth = third.copyAndModify(defender, CombatType.MELEE, damage -> maxHit);
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
	public int modifyAccuracy(Player attacker, Actor defender, int roll) {
		return (int) (roll * 1.75);
	}

	@Override
	public int modifyDamage(Player attacker, Actor defender, int damage) {
		return (int) (damage * 1.05);
	}

}
