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
public final class ZamorakGodsword extends PlayerMeleeStrategy {

	private static final Animation ANIMATION = new Animation(7070, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1221);
	private static final Graphic GRAPHIC_VICTIM = new Graphic(2104);
	private static final Graphic GRAPHIC_VICTIM_FAILED = new Graphic(339, 10);

	@Override
	public void attack(Player attacker, Actor defender, Hit h) {
		super.attack(attacker, defender, h);
		if(h.isAccurate()) {
			attacker.graphic(GRAPHIC);
			defender.graphic(GRAPHIC_VICTIM);
			if(!defender.isFrozen() && defender.size() == 1) {
				defender.freeze(20);
			}
		} else {
			defender.graphic(GRAPHIC_VICTIM_FAILED);
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
		return (int) (roll * 1.10);
	}

	@Override
	public int modifyDamage(Player attacker, Actor defender, int damage) {
		return (int) (damage * 1.40);
	}

}
