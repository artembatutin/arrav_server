package com.rageps.world.entity.actor.combat.strategy.npc.boss.glacor;

import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.strategy.CombatStrategy;
import com.rageps.world.entity.actor.combat.strategy.npc.MultiStrategy;
import com.rageps.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import com.rageps.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import com.rageps.world.entity.actor.mob.Mob;

/**
 * Created by Dave/Ophion
 * Date: 05/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class GlacorStrategy extends MultiStrategy {

	private static final Melee MELEE = new Melee();
	private static final Ranged RANGED = new Ranged();
	private static final Icicle ICICLE = new Icicle();

	private static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(MELEE, ICICLE, RANGED);
	private static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(ICICLE, RANGED);

	public GlacorStrategy() {
		currentStrategy = randomStrategy(NON_MELEE);
	}

	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer();
	}

	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		if(MELEE.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		currentStrategy.start(attacker, defender, hits);
	}

	/**
	 * Represents the Melee {@link CombatStrategy} of Glacor
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
	 * Represents the Ranged {@link CombatStrategy} of Glacor
	 */
	private static final class Ranged extends NpcRangedStrategy {

		public Ranged() {
			super(CombatProjectile.getDefinition(""));
		}

	}

	/**
	 * Represents the Icicle special attack {@link CombatStrategy} of Glacor
	 */
	private static final class Icicle extends NpcRangedStrategy {
		public Icicle() {
			super(CombatProjectile.getDefinition(""));
		}
	}
}
