package net.edge.content.combat.strategy.bandos;

import net.edge.task.Task;
import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.mob.impl.gwd.GeneralGraardor;

public final class SergeantGrimspikeCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(6154));
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || character.isDead() || victim.isDead())
					return;
				new Projectile(character, victim, 37, 44, 3, 43, 43, 0).sendProjectile();
			}
		});
		return new CombatHit(character, victim, 1, CombatType.RANGED, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Actor character) {
		return 7;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{6265};
	}

}
