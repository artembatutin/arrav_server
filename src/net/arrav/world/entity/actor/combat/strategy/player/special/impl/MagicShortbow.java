package net.arrav.world.entity.actor.combat.strategy.player.special.impl;

import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.Projectile;
import net.arrav.world.World;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.ranged.RangedWeaponType;
import net.arrav.world.entity.actor.combat.strategy.player.PlayerRangedStrategy;
import net.arrav.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class MagicShortbow extends PlayerRangedStrategy {

	private static final Animation ANIMATION = new Animation(1074, Animation.AnimationPriority.HIGH);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.animation(ANIMATION);
	}

	@Override
	public void attack(Player player, Actor defender, Hit hit) {
		//new Projectile(player, defender, 249, 58, 40, 43, 31, CombatType.RANGED).sendProjectile();
		new Projectile(player, defender, 249, 40, 10, 44, 35, CombatType.RANGED).sendProjectile();

		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				player.animation(new Animation(426, Animation.AnimationPriority.HIGH));
				new Projectile(player, defender, 249, 40, 15, 44, 35, CombatType.RANGED).sendProjectile();
				this.cancel();
			}
		});
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
	public CombatHit[] getHits(Player attacker, Actor defender) {
		return new CombatHit[]{nextRangedHit(attacker, defender, RangedWeaponType.SHOT), nextRangedHit(attacker, defender, RangedWeaponType.SHOT)};
	}

	@Override
	public boolean disableAnimations() {
		return true;
	}
}
