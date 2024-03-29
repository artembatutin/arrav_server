package net.arrav.world.entity.actor.combat.strategy.npc;

import net.arrav.world.Animation;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.strategy.basic.MeleeStrategy;
import net.arrav.world.entity.actor.mob.Mob;

public class NpcMeleeStrategy extends MeleeStrategy<Mob> {
	
	private static final NpcMeleeStrategy INSTANCE = new NpcMeleeStrategy();
	
	protected NpcMeleeStrategy() {
	}
	
	@Override
	public void start(Mob attacker, Actor defender, Hit[] hits) {
		attacker.animation(getAttackAnimation(attacker, defender));
	}
	
	@Override
	public void attack(Mob attacker, Actor defender, Hit hit) {
		CombatPoisonEffect.getPoisonType(attacker.getId()).ifPresent(p -> {
			if(attacker.getDefinition().poisonous()) {
				defender.poison(p);
			}
		});
	}
	
	@Override
	public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
		return attacker.getAttackDelay();
	}
	
	@Override
	public int getAttackDistance(Mob attacker, FightType fightType) {
		return 1;
	}
	
	@Override
	public CombatHit[] getHits(Mob attacker, Actor defender) {
		return new CombatHit[]{nextMeleeHit(attacker, defender, attacker.getDefinition().getMaxHit())};
	}
	
	@Override
	public Animation getAttackAnimation(Mob attacker, Actor defender) {
		return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
	
	public static NpcMeleeStrategy get() {
		return INSTANCE;
	}
	
}
