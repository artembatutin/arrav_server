package net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.armadyl;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.impl.godwars.KreeArra;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class FlockleaderGeerinStrategy extends NpcRangedStrategy {
	
	public FlockleaderGeerinStrategy() {
		super(CombatProjectile.getDefinition("Flockleader geerin"));
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer() && KreeArra.CHAMBER.inLocation(defender.getPosition());
		
	}
	
}
