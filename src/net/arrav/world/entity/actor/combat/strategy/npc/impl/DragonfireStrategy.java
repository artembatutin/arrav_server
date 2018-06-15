package net.arrav.world.entity.actor.combat.strategy.npc.impl;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatUtil;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMagicStrategy;
import net.arrav.world.entity.actor.mob.Mob;

public class DragonfireStrategy extends NpcMagicStrategy {
	
	public DragonfireStrategy(CombatProjectile projectileDefinition) {
		super(projectileDefinition);
	}
	
	@Override
	public int getAttackDistance(Mob attacker, FightType fightType) {
		return 1;
	}
	
	@Override
	public CombatHit[] getHits(Mob attacker, Actor defender) {
		return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender)};
	}
	
}