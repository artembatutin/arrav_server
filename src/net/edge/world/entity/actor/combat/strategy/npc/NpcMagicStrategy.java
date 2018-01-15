package net.edge.world.entity.actor.combat.strategy.npc;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatImpact;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.projectile.CombatProjectile;
import net.edge.world.entity.actor.combat.strategy.basic.MagicStrategy;
import net.edge.world.entity.actor.mob.Mob;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class NpcMagicStrategy extends MagicStrategy<Mob> {
	
	private final CombatProjectile projectileDefinition;
	
	/**
	 * The spell splash graphic.
	 */
	private static final Graphic SPLASH = new Graphic(85);
	
	public NpcMagicStrategy(CombatProjectile projectileDefinition) {
		this.projectileDefinition = projectileDefinition;
	}
	
	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		Animation animation = projectileDefinition.getAnimation().orElse(getAttackAnimation(attacker, defender));
		attacker.animation(animation);
		defender.toPlayer().message("anim: " +animation.getId());
		projectileDefinition.getStart().ifPresent(attacker::graphic);
		projectileDefinition.sendProjectile(attacker, defender, true);
	}
	
	@Override
	public void attack(Mob attacker, Actor defender, Hit hit) {
		Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
		Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, null);
		projectileDefinition.getEffect().filter(filter).ifPresent(execute);
		
		CombatPoisonEffect.getPoisonType(attacker.getId()).ifPresent(p -> {
			if(hit.isAccurate() && attacker.getDefinition().poisonous()) {
				defender.poison(p);
			}
		});
	}
	
	@Override
	public void hit(Mob attacker, Actor defender, Hit hit) {
		if(!hit.isAccurate()) {
			defender.graphic(SPLASH);
		} else {
			projectileDefinition.getEnd().ifPresent(defender::graphic);
		}
	}
	
	@Override
	public CombatHit[] getHits(Mob attacker, Actor defender) {
		return new CombatHit[]{nextMagicHit(attacker, defender, attacker.getDefinition().getMaxHit())};
	}
	
	@Override
	public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
		int delay = attacker.getDefinition().getAttackDelay();
		
		if(attacker.getPosition().getDistance(defender.getPosition()) > 4) {
			return 1 + delay;
		}
		
		return delay;
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
		return CombatType.MAGIC;
	}
	
}
