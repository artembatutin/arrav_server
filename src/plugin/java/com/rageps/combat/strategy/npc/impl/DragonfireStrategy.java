package com.rageps.combat.strategy.npc.impl;

import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.combat.strategy.npc.NpcMagicStrategy;
import com.rageps.world.entity.actor.mob.Mob;

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