package net.edge.world.entity.actor.combat.strategy;

import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.attack.FormulaFactory;
import net.edge.world.entity.actor.combat.attack.listener.CombatListener;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;

import static net.edge.world.entity.actor.combat.CombatUtil.getHitDelay;
import static net.edge.world.entity.actor.combat.CombatUtil.getHitsplatDelay;

public abstract class CombatStrategy<T extends Actor> implements CombatListener<T> {
	
	public abstract boolean withinDistance(T attacker, Actor defender);
	
	public abstract int getAttackDelay(T attacker, Actor defender, FightType fightType);
	
	public abstract int getAttackDistance(T attacker, FightType fightType);
	
	public abstract CombatHit[] getHits(T attacker, Actor defender);
	
	public abstract Animation getAttackAnimation(T attacker, Actor defender);
	
	@Override
	public abstract boolean canAttack(T attacker, Actor defender);
	
	@Override
	public void start(T attacker, Actor defender, Hit[] hits) {
	}
	
	@Override
	public void attack(T attacker, Actor defender, Hit hit) {
	}
	
	@Override
	public void hit(T attacker, Actor defender, Hit hit) {
	}

	@Override
	public void hitsplat(T attacker, Actor defender, Hit hit) {
	}

	@Override
	public void block(Actor attacker, T defender, Hit hit, CombatType combatType) {
	}
	
	@Override
	public void onDeath(Actor attacker, T defender, Hit hit) {
	}
	
	@Override
	public void finish(T attacker, Actor defender) {
	}
	
	public abstract CombatType getCombatType();
	
	protected final CombatHit nextMeleeHit(T attacker, Actor defender) {
		int hitDelay = getHitDelay(attacker, defender, getCombatType());
		int hitsplatDelay = getHitsplatDelay(getCombatType());
		return nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextMeleeHit(T attacker, Actor defender, int maxHit) {
		int hitDelay = getHitDelay(attacker, defender, getCombatType());
		int hitsplatDelay = getHitsplatDelay(getCombatType());
		return nextMeleeHit(attacker, defender, maxHit, hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender) {
		int hitDelay = getHitDelay(attacker, defender, getCombatType());
		int hitsplatDelay = getHitsplatDelay(getCombatType());
		return nextRangedHit(attacker, defender, hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender, int max) {
		int hitDelay = getHitDelay(attacker, defender, getCombatType());
		int hitsplatDelay = getHitsplatDelay(getCombatType());
		return nextRangedHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextMagicHit(T attacker, Actor defender, int max) {
		int hitDelay = getHitDelay(attacker, defender, getCombatType());
		int hitsplatDelay = getHitsplatDelay(getCombatType());
		return nextMagicHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}

	private CombatHit nextMeleeHit(T attacker, Actor defender, int maxHit, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextMeleeHit(attacker, defender, maxHit), hitDelay, hitsplatDelay);
	}

	private CombatHit nextMeleeHit(T attacker, Actor defender, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextMeleeHit(attacker, defender), hitDelay, hitsplatDelay);
	}

	private CombatHit nextRangedHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextRangedHit(attacker, defender, max), hitDelay, hitsplatDelay);
	}

	private CombatHit nextRangedHit(T attacker, Actor defender, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextRangedHit(attacker, defender), hitDelay, hitsplatDelay);
	}

	protected CombatHit nextMagicHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextMagicHit(attacker, defender, max), hitDelay, hitsplatDelay);
	}

}
