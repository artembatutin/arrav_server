package com.rageps.world.entity.actor.combat.strategy.player.special.impl;

import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public final class DragonScimitar extends PlayerMeleeStrategy {

	private static final Animation ANIMATION = new Animation(12031, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(2118, 100);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void attack(Player player, Actor target, Hit hit) {
		super.attack(player, target, hit);

		if(target.isPlayer() && hit.isAccurate()) {
			Player victim = target.toPlayer();
			Prayer.PROTECT_FROM_MAGIC.deactivate(victim);
			Prayer.PROTECT_FROM_MELEE.deactivate(victim);
			Prayer.PROTECT_FROM_MISSILES.deactivate(victim);
			player.message("You have been injured");
		}
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
	public int modifyAccuracy(Player attacker, Actor defender, int roll) {
		return roll * 2;
	}

}
