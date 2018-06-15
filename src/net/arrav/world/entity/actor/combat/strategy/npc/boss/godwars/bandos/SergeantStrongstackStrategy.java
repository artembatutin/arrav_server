package net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.bandos;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.impl.godwars.GeneralGraardor;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class SergeantStrongstackStrategy extends NpcMeleeStrategy {
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer() && GeneralGraardor.CHAMBER.inLocation(defender.getPosition());
	}
	
}
