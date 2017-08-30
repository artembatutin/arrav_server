package net.edge.world.entity.actor.combat.strategy.player.special;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;

public class GraniteMaul extends PlayerMeleeStrategy {

	private static final Animation ANIMATION = new Animation(1667, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(340);

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
		return new CombatHit[]{nextMeleeHit(attacker, defender)};
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Actor defender) {
		return ANIMATION;
	}

	@Override
	public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
		return 1;
	}
}
