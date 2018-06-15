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
public abstract class RangedStrategy<T extends Actor> extends CombatStrategy<T> {
	
	/**
	 * Base Experience multiplier.
	 * ON CHANGE: DO NOT FORGET TO CHANGE THE XP MULTIPLIER ON CANNON
	 * {@link net.arrav.content.object.cannon.MulticannonTask}
	 */
	private static final int BASE_EXPERIENCE_MULTIPLIER = 4;
	
	@Override
	public boolean withinDistance(T attacker, Actor defender) {
		if(attacker.getAttr().get("master_archery").getBoolean()) {
			return true;
		}
		
		MovementQueue movement = attacker.getMovementQueue();
		MovementQueue otherMovement = defender.getMovementQueue();
		FightType fightType = attacker.getCombat().getFightType();
		int distance = getAttackDistance(attacker, fightType);
		
		if(!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement() && !attacker.isFrozen()) {
			distance += 1;
			if(movement.isRunning()) {
				distance += 2;
			}
		}
		if(!new Boundary(attacker.getPosition(), attacker.size()).within(defender.getPosition(), defender.size(), distance)) {
			return false;
		}
		if(!World.getSimplePathChecker().checkProjectile(attacker.getPosition(), defender.getPosition())) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canAttack(T attacker, Actor defender) {
		return true;
	}
	
	public static void addCombatExperience(Player player, Hit... hits) {
		int exp = 0;
		for(Hit hit : hits) {
			exp += hit.getDamage();
		}
		
		exp = Math.round(exp / 10F);
		
		if(player.getCombat().getFightType() == FightType.FLARE) {
			exp *= 4;
			Skills.experience(player, exp / 3, Skills.HITPOINTS);
			Skills.experience(player, exp, Skills.RANGED);
		} else if(player.getCombat().getFightType() == FightType.BLAZE) {
			exp *= 2;
			Skills.experience(player, exp / 3, Skills.HITPOINTS);
			Skills.experience(player, exp, Skills.MAGIC);
		} else if(player.getCombat().getFightType() == FightType.SCORCH) {
			exp *= 4;
			Skills.experience(player, exp / 3, Skills.HITPOINTS);
			Skills.experience(player, exp, Skills.STRENGTH);
		} else {
			exp *= BASE_EXPERIENCE_MULTIPLIER;
			Skills.experience(player, exp / 3, Skills.HITPOINTS);
			
			switch(player.getCombat().getFightType().getStyle()) {
				case DEFENSIVE:
					exp /= 2;
					Skills.experience(player, exp, Skills.DEFENCE);
					Skills.experience(player, exp, Skills.RANGED);
					break;
				default:
					Skills.experience(player, exp, Skills.RANGED);
					break;
			}
		}
	}
	
}
