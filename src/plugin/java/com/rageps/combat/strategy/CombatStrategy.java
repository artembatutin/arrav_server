package com.rageps.combat.strategy;

import com.rageps.world.entity.actor.combat.formula.FormulaFactory;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatConstants;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.combat.listener.CombatListener;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.ranged.RangedWeaponType;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Equipment;

import static com.rageps.world.entity.actor.combat.CombatUtil.getHitDelay;
import static com.rageps.world.entity.actor.combat.CombatUtil.getHitsplatDelay;

public abstract class CombatStrategy<T extends Actor> implements CombatListener<T> {
	
	public abstract int getAttackDelay(T attacker, Actor defender, FightType fightType);
	
	public abstract int getAttackDistance(T attacker, FightType fightType);
	
	public abstract CombatHit[] getHits(T attacker, Actor defender);
	
	public abstract Animation getAttackAnimation(T attacker, Actor defender);
	
	@Override
	public abstract boolean canAttack(T attacker, Actor defender);
	
	@Override
	public boolean canOtherAttack(Actor attacker, T defender) {
		return true;
	}
	
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
	public void block(Actor attacker, T defender, Hit hit, CombatType combatType) {
	}
	
	@Override
	public void preDeath(Actor attacker, T defender, Hit hit) {
	}
	
	@Override
	public void onDeath(Actor attacker, T defender, Hit hit) {
	}
	
	@Override
	public void preKill(T attacker, Actor defender, Hit hit) {
	}
	
	@Override
	public void onKill(T attacker, Actor defender, Hit hit) {
	}
	
	@Override
	public void hitsplat(T attacker, Actor defender, Hit hit) {
	}
	
	@Override
	public void finishOutgoing(T attacker, Actor defender) {
	}
	
	@Override
	public void finishIncoming(Actor attacker, T defender) {
	}
	
	public abstract CombatType getCombatType();
	
	/* ********** SIMPLIFIED ********** */
	
	protected CombatHit nextMeleeHit(T attacker, Actor defender) {
		int max = FormulaFactory.getMaxHit(attacker, defender, CombatType.MELEE);
		return nextMeleeHit(attacker, defender, max);
	}
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender) {
		int max = FormulaFactory.getMaxHit(attacker, defender, CombatType.RANGED);
		return nextRangedHit(attacker, defender, max);
	}
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender, RangedWeaponType type) {
		if(type == RangedWeaponType.THROWN) {
			Item weapon = attacker.toPlayer().getEquipment().get(Equipment.WEAPON_SLOT);
			attacker.toPlayer().setBonus(CombatConstants.BONUS_RANGED_STRENGTH, weapon.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH]);
		}
		int max = FormulaFactory.getMaxHit(attacker, defender, CombatType.RANGED);
		return nextRangedHit(attacker, defender, max);
	}
	
	protected final CombatHit nextMagicHit(T attacker, Actor defender) {
		int max = FormulaFactory.getMaxHit(attacker, defender, CombatType.MAGIC);
		return nextMagicHit(attacker, defender, max);
	}
	
	/* ********** MAX HITS ********** */
	
	protected CombatHit nextMeleeHit(T attacker, Actor defender, int max) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.MELEE);
		int hitsplatDelay = getHitsplatDelay(CombatType.MELEE);
		return nextMeleeHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender, int max) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.RANGED);
		int hitsplatDelay = getHitsplatDelay(CombatType.RANGED);
		return nextRangedHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextMagicHit(T attacker, Actor defender, int max) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.MAGIC);
		int hitsplatDelay = getHitsplatDelay(CombatType.MAGIC);
		return nextMagicHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}
	
	/* ********** MAX HITS & COMBAT PROJECTILES ********** */
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender, int max, CombatProjectile projectile) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.RANGED);
		int hitsplatDelay = getHitsplatDelay(CombatType.RANGED);
		hitDelay = projectile.getHitDelay().orElse(hitDelay);
		hitsplatDelay = projectile.getHitsplatDelay().orElse(hitsplatDelay);
		return nextRangedHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}
	
	protected CombatHit nextMagicHit(T attacker, Actor defender, int max, CombatProjectile projectile) {
		int hitDelay = getHitDelay(attacker, defender, CombatType.MAGIC);
		int hitsplatDelay = getHitsplatDelay(CombatType.MAGIC);
		hitDelay = projectile.getHitDelay().orElse(hitDelay);
		hitsplatDelay = projectile.getHitsplatDelay().orElse(hitsplatDelay);
		return nextMagicHit(attacker, defender, max, hitDelay, hitsplatDelay);
	}
	
	/* ********** COMBAT PROJECTILES ********** */
	
	protected final CombatHit nextRangedHit(T attacker, Actor defender, CombatProjectile projectile) {
		int max = FormulaFactory.getMaxHit(attacker, defender, CombatType.RANGED);
		return nextRangedHit(attacker, defender, max, projectile);
	}
	
	protected CombatHit nextMagicHit(T attacker, Actor defender, CombatProjectile projectile) {
		int max = projectile.getMaxHit();
		return nextMagicHit(attacker, defender, max, projectile);
	}
	
	/* ********** BASE METHODS ********** */
	
	protected CombatHit nextMeleeHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextMeleeHit(attacker, defender, max), hitDelay, hitsplatDelay);
	}
	
	protected CombatHit nextRangedHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextRangedHit(attacker, defender, max), hitDelay, hitsplatDelay);
	}
	
	protected final CombatHit nextMagicHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
		return new CombatHit(FormulaFactory.nextMagicHit(attacker, defender, max), hitDelay, hitsplatDelay);
	}
	
}
