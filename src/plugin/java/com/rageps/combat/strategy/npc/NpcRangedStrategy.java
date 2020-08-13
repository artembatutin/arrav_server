package com.rageps.combat.strategy.npc;

import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.combat.strategy.basic.RangedStrategy;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatImpact;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.mob.Mob;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class NpcRangedStrategy extends RangedStrategy<Mob> {
	
	private final CombatProjectile projectileDefinition;
	
	public NpcRangedStrategy(CombatProjectile projectileDefinition) {
		this.projectileDefinition = projectileDefinition;
	}
	
	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		Animation animation = projectileDefinition.getAnimation().orElse(getAttackAnimation(attacker, defender));
		attacker.animation(animation);
		projectileDefinition.getStart().ifPresent(attacker::graphic);
		projectileDefinition.sendProjectile(attacker, defender, true);
	}
	
	@Override
	public void attack(Mob attacker, Actor defender, Hit hit) {
		Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
		Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, null);
		projectileDefinition.getEffect().filter(filter).ifPresent(execute);
		CombatPoisonEffect.getPoisonType(attacker.getId()).ifPresent(p -> {
			if(attacker.getDefinition().poisonous()) {
				defender.poison(p);
			}
		});
	}
	
	@Override
	public void hit(Mob attacker, Actor defender, Hit hit) {
		projectileDefinition.getEnd().ifPresent(defender::graphic);
	}
	
	@Override
	public CombatHit[] getHits(Mob attacker, Actor defender) {
		return new CombatHit[]{nextRangedHit(attacker, defender, projectileDefinition.getMaxHit())};
	}
	
	@Override
	public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
		return attacker.getAttackDelay();
	}
	
	@Override
	public int getAttackDistance(Mob attacker, FightType fightType) {
		return 10;
	}
	
	@Override
	public Animation getAttackAnimation(Mob attacker, Actor defender) {
		return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
	
}
