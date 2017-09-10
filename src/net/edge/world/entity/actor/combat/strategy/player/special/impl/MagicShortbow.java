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
import net.edge.world.entity.actor.combat.ranged.RangedWeaponType;
import net.edge.world.entity.actor.combat.strategy.player.PlayerRangedStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class MagicShortbow extends PlayerRangedStrategy {

	private static final Animation ANIMATION = new Animation(426, Animation.AnimationPriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(250, 100);
	private static final CombatModifier MODIFIER = new CombatModifier();

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.animation(ANIMATION);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void attack(Player player, Actor defender, Hit hit) {
		new Projectile(player, defender, 249, 58, 40, 43, 31, CombatType.RANGED).sendProjectile();

		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				player.animation(new Animation(426, Animation.AnimationPriority.HIGH));
				new Projectile(player, defender, 249, 48, 30, 43, 31, CombatType.RANGED).sendProjectile();
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
	public Optional<CombatModifier> getModifier(Player attacker) {
		return Optional.of(MODIFIER);
	}

	@Override
	public boolean disableAnimations() {
		return true;
	}
}
