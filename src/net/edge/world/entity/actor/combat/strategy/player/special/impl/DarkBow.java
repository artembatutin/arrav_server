package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.CombatModifier;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.ranged.RangedAmmunition;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponType;
import net.edge.world.entity.actor.combat.strategy.player.PlayerRangedStrategy;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author http://www.rune-server.org/members/ophion/
 * @since 2-9-2017.
 */
public class DarkBow extends PlayerRangedStrategy {

	private static final Animation ANIMATION = new Animation(426, Animation.AnimationPriority.HIGH);
	private static final CombatModifier DESCENT_OF_DARKNESS_MODIFIER = new CombatModifier().damage(0.30);
	private static final CombatModifier DESCENT_OF_DRAGONS_MODIFIER = new CombatModifier().damage(0.50);
	private static final Graphic DRAW_BACK = new Graphic(1111, 50);

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.animation(ANIMATION);
	}

	@Override
	public void attack(Player player, Actor defender, Hit hit) {
		//player.graphic(DRAW_BACK);
		int projectileId = (player.rangedAmmo == RangedAmmunition.DRAGON_ARROW ? 1099 : 1101);
		new Projectile(player, defender, projectileId, 80, 47, 43, 31, CombatType.RANGED).sendProjectile();

		World.get().submit(new Task(1, false) {
				@Override
				public void execute() {
					new Projectile(player, defender, projectileId, 70, 20, 50, 31, CombatType.RANGED).sendProjectile();
					this.cancel();
				}
			});
	}

	@Override
	public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
		return 8;
	}
	
	@Override
	public Animation getAttackAnimation(Player attacker, Actor defender) {
		return ANIMATION;
	}

	@Override
	public CombatHit[] getHits(Player attacker, Actor defender) {
		CombatHit first = nextRangedHit(attacker, defender, RangedWeaponType.SHOT);
		CombatHit second = nextRangedHit(attacker, defender, RangedWeaponType.SHOT);
		second.setHitDelay(second.getHitDelay() + 1);
		if(attacker.rangedAmmo == RangedAmmunition.DRAGON_ARROW) {
			if (first.getDamage() < 80) {
				first.modifyDamage(damage -> 80);
			}

			if (second.getDamage() < 80) {
				second.modifyDamage(damage -> 80);
			}
		} else {
			if (first.getDamage() < 50) {
				first.modifyDamage(damage -> 50);
			}

			if (second.getDamage() < 50) {
				second.modifyDamage(damage -> 50);
			}
		}
		World.get().submit(new Task(first.getHitDelay(), false) {
			@Override
			public void execute() {
				defender.graphic(new Graphic(1100));
				this.cancel();
			}
		});
		return new CombatHit[]{first, second};
	}

	@Override
	public Optional<CombatModifier> getModifier(Player attacker) {
		return Optional.of(attacker.rangedAmmo == RangedAmmunition.DRAGON_ARROW ? DESCENT_OF_DRAGONS_MODIFIER : DESCENT_OF_DARKNESS_MODIFIER);
	}

	@Override
	public boolean disableAnimations() {
		return true;
	}
}
