package net.edge.world.entity.actor.combat.strategy.npc.boss;

import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.combat.strategy.npc.MultiStrategy;
import net.edge.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.world.entity.actor.combat.strategy.npc.impl.DragonfireStrategy;
import net.edge.world.entity.actor.mob.Mob;

import static net.edge.world.entity.actor.combat.projectile.CombatProjectile.getDefinition;

/** @author Michael | Chex */
public class KingBlackDragonStrategy extends MultiStrategy {
	
	private static final Melee STAB = new Melee();
	private static final CrushMelee CRUSH = new CrushMelee();
	private static final Dragonfire DRAGONFIRE = new Dragonfire();
	private static final Poison POISON = new Poison();
	private static final Freeze FREEZE = new Freeze();
	private static final Shock SHOCK = new Shock();
	
	private static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(CRUSH, STAB, DRAGONFIRE, POISON, FREEZE, SHOCK);
	private static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(DRAGONFIRE, POISON, FREEZE, SHOCK);
	
	public KingBlackDragonStrategy() {
		currentStrategy = randomStrategy(NON_MELEE);
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		if(!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		currentStrategy.finish(attacker, defender);
		if(STAB.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(NON_MELEE);
		}
	}

	@Override
	public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}
	
	@Override
	public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
		return attacker.getDefinition().getAttackDelay();
	}
	
	private static final class CrushMelee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(80, Animation.AnimationPriority.HIGH);
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 1;
		}
		
		@Override
		public Animation getAttackAnimation(Mob attacker, Actor defender) {
			return ANIMATION;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{nextMeleeHit(attacker, defender)};
		}
	}
	
	private static final class Melee extends NpcMeleeStrategy {
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 1;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{nextMeleeHit(attacker, defender)};
		}
	}
	
	private static final class Dragonfire extends DragonfireStrategy {
		
		Dragonfire() {
			super(getDefinition("KBD fire"));
		}
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 10;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
		}
		
	}
	
	private static final class Freeze extends DragonfireStrategy {
		
		Freeze() {
			super(getDefinition("KBD freeze"));
		}
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 10;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
		}
		
	}
	
	private static final class Shock extends DragonfireStrategy {
		
		Shock() {
			super(getDefinition("KBD shock"));
		}
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 10;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
		}
		
	}
	
	private static final class Poison extends DragonfireStrategy {
		
		Poison() {
			super(getDefinition("KBD poison"));
		}
		
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 10;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 650, true)};
		}
	}
	
}
