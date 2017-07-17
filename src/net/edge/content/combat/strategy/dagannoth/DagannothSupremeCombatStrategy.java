package net.edge.content.combat.strategy.dagannoth;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.node.EntityState;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Projectile;

import java.util.Arrays;
import java.util.Objects;

public final class DagannothSupremeCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public void incomingAttack(Actor character, Actor attacker, CombatHit data) {
		if(data.getType().equals(CombatType.RANGED) || data.getType().equals(CombatType.MAGIC)) {
			attacker.toPlayer().message("Your attacks are completely blocked...");
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
		World.get().submit(new Task(1, false) {
			@Override
			protected void execute() {
				this.cancel();
				if(character.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || character.isDead() || victim.isDead())
					return;
				new Projectile(character, victim, 1937, 44, 4, 60, 43, 0).sendProjectile();
			}
		});
		return new CombatHit(character, victim, 1, CombatType.RANGED, true, 2);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Actor character) {
		return 5;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{2881};
	}

}
