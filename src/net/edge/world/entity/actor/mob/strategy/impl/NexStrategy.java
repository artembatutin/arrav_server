package net.edge.world.entity.actor.mob.strategy.impl;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.impl.SkeletalHorror;
import net.edge.world.entity.actor.mob.impl.nex.Nex;
import net.edge.world.entity.actor.mob.strategy.DynamicStrategy;
import net.edge.world.entity.actor.player.Player;

public final class NexStrategy extends DynamicStrategy<Nex> {
	
	
	public NexStrategy(Nex npc) {
		super(npc);
	}
	
	@Override
	public boolean canOutgoingAttack(Actor victim) {
		return true;
	}
	
	@Override
	public CombatHit outgoingAttack(Actor victim) {
		if(victim.isPlayer()) {
			Player player = victim.toPlayer();
			Skill prayer = player.getSkills()[Skills.PRAYER];
			prayer.decreaseLevel(1);
			Skills.refresh(player, Skills.PRAYER);
		}
		return new CombatHit(npc, victim, 1, CombatType.MELEE, true);
	}
	
	@Override
	public void incomingAttack(Actor attacker, CombatHit data) {
	
	}
	
	@Override
	public int attackDelay() {
		return 3;
	}
	
	@Override
	public int attackDistance() {
		return 2;
	}

}
