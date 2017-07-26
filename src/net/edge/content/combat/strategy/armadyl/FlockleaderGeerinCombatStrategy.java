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
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || character.isDead() || victim.isDead())
					return;
				new Projectile(character, victim, 1837, 44, 3, 43, 43, 0).sendProjectile();
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
	public int[] getMobs() {
		return new int[]{6225};
	}

}
