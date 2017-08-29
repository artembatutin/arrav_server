package net.edge.world.entity.actor.combat.strategy.player;

import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.basic.MeleeStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.container.impl.Equipment;

public class PlayerMeleeStrategy extends MeleeStrategy<Player> {
	
	public static final PlayerMeleeStrategy INSTANCE = new PlayerMeleeStrategy();
	
	protected PlayerMeleeStrategy() {
	}
	
	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		if(attacker.isSpecialActivated()) {
			attacker.getCombatSpecial().drain(attacker);
		}
		attacker.animation(getAttackAnimation(attacker, defender));
		addCombatExperience(attacker, hits);
	}
	
	@Override
	public void hit(Player attacker, Actor defender, Hit hit) {
		CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(Equipment.WEAPON_SLOT)).ifPresent(p -> {
			if(hit.getDamage() > 0) {
				defender.poison(p);
			}
		});
	}
	
	@Override
	public CombatHit[] getHits(Player attacker, Actor defender) {
		return new CombatHit[]{nextMeleeHit(attacker, defender)};
	}
	
	@Override
	public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
		return attacker.getAttackDelay();
	}
	
	@Override
	public int getAttackDistance(Player attacker, FightType fightType) {
		if(attacker.getWeapon() == WeaponInterface.HALBERD)
			return 2;
		return 1;
	}
	
	@Override
	public Animation getAttackAnimation(Player attacker, Actor defender) {
		FightType fightType = attacker.getCombat().getFightType();
		int animation;
		if(attacker.getWeaponAnimation() != null && !attacker.getCombat().getFightType().isAnimationPrioritized()) {
			animation = attacker.getWeaponAnimation().getAttacking()[fightType.getStyle().ordinal()];
		} else {
			animation = fightType.getAnimation();
		}
		return new Animation(animation, Animation.AnimationPriority.HIGH);
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
}
