package net.arrav.world.entity.actor.combat.strategy.npc.boss.dagannoth;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import net.arrav.world.entity.actor.mob.Mob;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class DagannothSupremeStrategy extends NpcRangedStrategy {
	
	public DagannothSupremeStrategy() {
		super(CombatProjectile.getDefinition("Dagannoth Supreme"));
	}
	
	@Override
	public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
		if(attacker.getStrategy().getCombatType().equals(CombatType.RANGED) || attacker.getStrategy().getCombatType().equals(CombatType.MAGIC)) {
			Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			attacker.toPlayer().message("Your attacks are completely blocked...");
		}
		defender.getCombat().attack(attacker);
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer();
	}
	
}
