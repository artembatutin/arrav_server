package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.RangedStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class NpcRangedStrategy extends RangedStrategy<Mob> {

	private final CombatProjectileDefinition projectileDefinition;

	public NpcRangedStrategy(CombatProjectileDefinition projectileDefinition) {
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
		Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
		Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit, null);
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
		return new CombatHit[]{nextRangedHit(attacker, defender, attacker.getDefinition().getMaxHit())};
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
