package net.edge.content.combat.strategy.basic;

import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.skill.Skills;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.move.MovementQueue;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Boundary;

/**
 * @author Michael | Chex
 */
public abstract class MagicStrategy<T extends Actor> extends CombatStrategy<T> {

	private static final int BASE_EXPERIENCE_MULTIPLIER = 2;

	@Override
	public boolean withinDistance(T attacker, Actor defender) {
		FightType fightType = attacker.getCombat().getFightType();
		MovementQueue movement = attacker.getMovementQueue();
		MovementQueue otherMovement = defender.getMovementQueue();
		int distance = getAttackDistance(attacker, fightType);
		if(!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement() && !attacker.isFrozen()) {
			distance += 1;
			if(movement.isRunning()) {
				distance += 2;
			}
		}
		if(new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance)) {
			if(!World.getSimplePathChecker().checkProjectile(attacker.getPosition(), defender.getPosition())) {
				return false;
			}
			attacker.getMovementQueue().reset();
			attacker.setFollowing(false);
			attacker.faceEntity(defender);
			return true;
		}
		return false;
	}

	@Override
	public boolean canAttack(T attacker, Actor defender) {
		return true;
	}

	protected static void addCombatExperience(Player player, double base, Hit... hits) {
		int exp = 0;
		for(Hit hit : hits) {
			exp += hit.getDamage();
		}

		exp = Math.round(exp / 10F);
		exp *= BASE_EXPERIENCE_MULTIPLIER;
		exp += base;
		Skills.experience(player, exp, Skills.MAGIC);
		Skills.experience(player, exp / 3, Skills.HITPOINTS);
	}

}
