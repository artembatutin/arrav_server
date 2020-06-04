package com.rageps.combat.strategy.npc.boss.dagannoth;

import com.rageps.combat.strategy.MobCombatStrategyMeta;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.npc.NpcRangedStrategy;
import com.rageps.world.entity.actor.mob.Mob;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
@MobCombatStrategyMeta(ids = 2881)
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
