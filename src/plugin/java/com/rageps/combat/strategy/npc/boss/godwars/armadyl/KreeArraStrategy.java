package com.rageps.combat.strategy.npc.boss.godwars.armadyl;

import com.rageps.combat.strategy.MobCombatStrategyMeta;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.Graphic;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.combat.strategy.npc.MultiStrategy;
import com.rageps.combat.strategy.npc.NpcMagicStrategy;
import com.rageps.combat.strategy.npc.NpcMeleeStrategy;
import com.rageps.combat.strategy.npc.NpcRangedStrategy;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.impl.godwars.KreeArra;

/**
 * This strategy doesn't have a {@link MobCombatStrategyMeta} because a new instance of this strategy is assigned
 * to the Mob in it's constructor.
 */
public class KreeArraStrategy extends MultiStrategy {
	
	private static final Melee MELEE = new Melee();
	private static final Ranged RANGED = new Ranged();
	private static final Magic MAGIC = new Magic();
	
	private static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(MELEE, MAGIC, RANGED);
	private static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(MAGIC, RANGED);
	
	public KreeArraStrategy() {
		currentStrategy = randomStrategy(NON_MELEE);
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer() && KreeArra.CHAMBER.inLocation(defender.getPosition());
	}
	
	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		if(MELEE.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		KreeArra.AVIANSIES.forEach(aviansie -> {
			if(!aviansie.isDead() && aviansie.getState() == EntityState.ACTIVE) {
				aviansie.getCombat().attack(defender);
			}
		});
		
		currentStrategy.start(attacker, defender, hits);
	}
	
	/**
	 * Represents the Melee {@link CombatStrategy} of {@link KreeArra}
	 */
	private static final class Melee extends NpcMeleeStrategy {
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 2;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{nextMeleeHit(attacker, defender)};
		}
	}
	
	/**
	 * Represents the Ranged {@link CombatStrategy} of {@link KreeArra}
	 */
	private static final class Ranged extends NpcRangedStrategy {
		
		public Ranged() {
			super(CombatProjectile.getDefinition("KreeArra Ranged"));
		}
		
		@Override
		public void finishOutgoing(Mob attacker, Actor defender) {
			if(RandomUtils.inclusive(100) < 60) {
				return;
			}
			Position position = TraversalMap.getRandomNearby(defender.getPosition(), attacker.getPosition(), 2);
			defender.getMovementQueue().reset();
			if(position != null)
				defender.move(position);
			defender.graphic(new Graphic(128));
		}
		
	}
	
	/**
	 * Represents the Magic {@link CombatStrategy} of {@link KreeArra}
	 */
	private static final class Magic extends NpcMagicStrategy {
		public Magic() {
			super(CombatProjectile.getDefinition("KreeArra Magic"));
		}
	}
}
