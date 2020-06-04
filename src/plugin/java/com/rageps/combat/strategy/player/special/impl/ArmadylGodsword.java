package com.rageps.combat.strategy.player.special.impl;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.player.PlayerMeleeStrategy;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class ArmadylGodsword extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(11989, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(2113);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
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
		return (int) (roll * 1.25);
	}

	@Override
	public int modifyDamage(Player attacker, Actor defender, int damage) {
		return (int) (damage * 1.375);
	}

}
