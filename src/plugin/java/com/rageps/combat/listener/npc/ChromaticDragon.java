package com.rageps.combat.listener.npc;

import com.rageps.combat.listener.NpcCombatListenerSignature;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.combat.strategy.npc.NpcMeleeStrategy;
import com.rageps.combat.strategy.npc.impl.DragonfireStrategy;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.mob.Mob;

import static com.rageps.world.entity.actor.combat.CombatUtil.createStrategyArray;
import static com.rageps.world.entity.actor.combat.CombatUtil.randomStrategy;

/**
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {
		/* Green */ 941, 4677, 4678, 4679, 4680, 10604, 10605, 10606, 10607, 10608, 10609,
		/* Red */ 53, 4669, 4670, 4671, 4672, 10815, 10816, 10817, 10818, 10819, 10820,
		/* Blue */ 55, 4681, 4682, 4683, 4684, 5178,
		/* Black */ 54, 4673, 4674, 4675, 4676, 10219, 10220, 10221, 10222, 10223, 10224})
public class ChromaticDragon extends SimplifiedListener<Mob> {
	private static DragonfireStrategy DRAGONFIRE;
	private static CombatStrategy<Mob>[] STRATEGIES;
	
	static {
		try {
			DRAGONFIRE = new DragonfireStrategy(CombatProjectile.getDefinition("Chromatic dragonfire"));
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
}
