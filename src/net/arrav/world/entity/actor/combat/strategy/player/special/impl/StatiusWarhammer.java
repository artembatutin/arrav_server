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
public class StatiusWarhammer extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(10505, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1840);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void attack(Player player, Actor target, Hit hit) {
		if(target.isPlayer() && hit.isAccurate()) {
			Player victim = target.toPlayer();
			int currentDef = victim.getSkills()[Skills.DEFENCE].getRealLevel();
			int defDecrease = (int) (currentDef * 0.11);
			if((currentDef - defDecrease) <= 0 || currentDef <= 0)
				return;
			victim.getSkills()[Skills.DEFENCE].decreaseLevelReal(defDecrease);
			victim.message("Your opponent has reduced your Defence level.");
			player.message("Your hammer forces some of your opponent's defences to break.");
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
		return (int) (roll * 1.23);
	}

	@Override
	public int modifyDamage(Player attacker, Actor defender, int damage) {
		return (int) (damage * 1.25);
	}

}
