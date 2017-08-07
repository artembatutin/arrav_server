package net.edge.content.combat.strategy.armadyl;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.mob.impl.gwd.KreeArra;

public final class FlockleaderGeerinCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		actor.animation(new Animation(actor.toMob().getDefinition().getAttackAnimation()));
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(actor.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || actor.isDead() || victim.isDead())
					return;
				new Projectile(actor, victim, 1837, 44, 3, 43, 43, 0).sendProjectile();
			}
		});
		return new CombatHit(actor, victim, 1, CombatType.RANGED, true);
	}

	@Override
	public int attackDelay(Actor actor) {
		return actor.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor actor) {
		return 7;
	}

	@Override
	public int[] getMobs() {
		return new int[]{6225};
	}

}
