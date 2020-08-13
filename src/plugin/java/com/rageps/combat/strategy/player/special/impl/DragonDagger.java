package com.rageps.combat.strategy.player.special.impl;

import com.rageps.content.achievements.Achievement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.player.PlayerMeleeStrategy;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class DragonDagger extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1062, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(252, 100);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void finishOutgoing(Player attacker, Actor defender) {
		Achievement.DRAGON_DAGGER.inc(attacker);
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
	public int modifyAccuracy(Player attacker, Actor defender, int roll) {
		return roll * 5 / 4;
	}

	@Override
	public int modifyDamage(Player attacker, Actor defender, int damage) {
		return damage * 23 / 20;
	}

}
