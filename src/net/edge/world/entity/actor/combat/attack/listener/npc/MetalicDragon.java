package net.edge.world.entity.actor.combat.attack.listener.npc;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.attack.listener.NpcCombatListenerSignature;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.world.entity.actor.combat.strategy.npc.impl.DragonfireStrategy;
import net.edge.world.entity.actor.mob.Mob;

import static net.edge.world.entity.actor.combat.CombatUtil.createStrategyArray;
import static net.edge.world.entity.actor.combat.CombatUtil.randomStrategy;
import static net.edge.world.entity.actor.combat.projectile.CombatProjectile.getDefinition;

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
			super(getDefinition("Metalic dragonfire"));
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
