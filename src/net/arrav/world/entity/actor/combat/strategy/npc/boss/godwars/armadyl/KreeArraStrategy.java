package net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.armadyl;

import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Graphic;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.strategy.CombatStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.MultiStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMagicStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcRangedStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.impl.godwars.KreeArra;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

/**
 * Created by Dave/Ophion
 * Date: 05/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
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
