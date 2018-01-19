package net.arrav.world.entity.actor.combat.strategy.basic;

import net.arrav.content.skill.Skills;
import net.arrav.world.World;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.strategy.CombatStrategy;
import net.arrav.world.entity.actor.move.MovementQueue;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Boundary;

/**
 * @author Michael | Chex
 */
public abstract class MagicStrategy<T extends Actor> extends CombatStrategy<T> {

	private static final int BASE_EXPERIENCE_MULTIPLIER = 2;
	
	@Override
	public boolean withinDistance(T attacker, Actor defender) {
		MovementQueue movement = attacker.getMovementQueue();
		MovementQueue otherMovement = defender.getMovementQueue();
		FightType fightType = attacker.getCombat().getFightType();
		int distance = getAttackDistance(attacker, fightType);

		if(!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement() && !attacker.isFrozen()) {
			distance += 1;
		}
		if (movement.isRunning()) {
			distance += 1;
		}

		if(!new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance)) {
			return false;
		}
		return World.getSimplePathChecker().checkProjectile(attacker.getPosition(), defender.getPosition());
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
