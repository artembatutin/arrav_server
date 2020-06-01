package com.rageps.world.entity.actor.combat.attack.listener.npc;

import com.rageps.world.entity.actor.combat.attack.listener.NpcCombatListenerSignature;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.combat.strategy.CombatStrategy;
import com.rageps.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import com.rageps.world.entity.actor.combat.strategy.npc.impl.DragonfireStrategy;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.attack.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.mob.Mob;

import static com.rageps.world.entity.actor.combat.CombatUtil.createStrategyArray;
import static com.rageps.world.entity.actor.combat.CombatUtil.randomStrategy;

/**
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {1590, 1591, 1592, 3590, 5363, 8424, 10776, 10777, 10778, 10779, 10780, 10781})
public class MetalicDragon extends SimplifiedListener<Mob> {
	
	private static Dragonfire DRAGONFIRE;
	private static CombatStrategy<Mob>[] STRATEGIES;
	
	static {
		try {
			DRAGONFIRE = new Dragonfire();
			STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), DRAGONFIRE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		if(!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(DRAGONFIRE);
		}
		return attacker.getStrategy().canAttack(attacker, defender);
	}
	
	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		if(!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(DRAGONFIRE);
		} else {
			attacker.setStrategy(randomStrategy(STRATEGIES));
		}
	}
	
	private static class Dragonfire extends DragonfireStrategy {
		private Dragonfire() {
			super(CombatProjectile.getDefinition("Metalic dragonfire"));
		}
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 10;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 600, false)};
		}
	}
	
}
