package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.impl.skeletal.SkeletalHorror;
import net.edge.world.entity.actor.player.Player;

public final class SkeletalHorrorCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return true;
	}
	
	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		if(SkeletalHorror.horror != null) {
			if(RandomUtils.inclusive(5) == 1) {
				SkeletalHorror.horror.animation(new Animation(12061));
				SkeletalHorror.horror.minion();
			} else if(RandomUtils.inclusive(10) == 1) {
				SkeletalHorror.horror.animation(new Animation(12061));
				SkeletalHorror.horror.bones();
			}
		}
		if(victim.isPlayer()) {
			Player player = victim.toPlayer();
			Skill prayer = player.getSkills()[Skills.PRAYER];
			prayer.decreaseLevel(1);
			Skills.refresh(player, Skills.PRAYER);
		}
		return new CombatHit(actor, victim, 1, CombatType.MELEE, true);
	}
	
	@Override
	public int attackDelay(Actor actor) {
		return 3;
	}
	
	@Override
	public int attackDistance(Actor actor) {
		return 2;
	}
	
	@Override
	public int[] getMobs() {
		return new int[]{9177};
	}

}
