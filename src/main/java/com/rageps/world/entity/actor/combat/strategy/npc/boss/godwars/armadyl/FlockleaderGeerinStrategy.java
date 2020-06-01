package com.rageps.world.entity.actor.combat.strategy.npc.boss.godwars.armadyl;

import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.impl.godwars.KreeArra;

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
