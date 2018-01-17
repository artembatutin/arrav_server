package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.content.achievements.Achievement;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.player.Player;

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
