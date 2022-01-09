package com.rageps.combat.strategy.npc.boss.godwars.armadyl;

import com.rageps.combat.strategy.MobCombatStrategyMeta;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.strategy.npc.NpcMagicStrategy;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.impl.godwars.KreeArra;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
@MobCombatStrategyMeta(ids = 6223)
public class WingmanSkreeStrategy extends NpcMagicStrategy {
	
	public WingmanSkreeStrategy() {
		super(CombatProjectile.getDefinition("Wingman skree"));
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer() && KreeArra.CHAMBER.inArea(defender.getPosition());
		
	}
	
	@Override
	public boolean hitBack() {
		return false;
	}
}
