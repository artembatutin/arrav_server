package net.arrav.world.entity.actor.combat.strategy.player.special.impl;

import net.arrav.content.skill.Skills;
import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.arrav.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public final class SaradominGodsword extends PlayerMeleeStrategy {

	private static final Graphic GRAPHIC = new Graphic(1220);
	private static final Animation ANIMATION = new Animation(7071, Animation.AnimationPriority.HIGH);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void attack(Player player, Actor defender, Hit h) {
		super.attack(player, defender, h);
		if(h.isAccurate()) {
			int hitpoints = h.getDamage() / 2;//50%
			int prayer = h.getDamage() / 3;//25%
			player.getSkills()[Skills.HITPOINTS].increaseLevel(hitpoints < 100 ? 100 : hitpoints, 990);
			player.getSkills()[Skills.PRAYER].increaseLevel(prayer < 5 ? 5 : prayer / 10, 99);
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
		return (int) (damage * 0.80);
	}

}
