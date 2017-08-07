package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.Strategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;

import java.util.Arrays;

public final class Dessourt implements Strategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return actor.isMob() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		victim.animation(new Animation(3508));
		return new CombatHit(actor, victim, 1, CombatType.MELEE, false);
	}
	
	@Override
	public void incomingAttack(Actor actor, Actor victim, CombatHit data) {
		if(data.getType() == CombatType.MELEE) {
			Arrays.stream(data.getHits()).forEach(hit -> hit.setDamage(hit.getDamage() / 2));
			actor.graphic(new Graphic(550));
			actor.forceChat("Hssssssssssss");
		}
	}

	@Override
	public int attackDelay(Actor actor) {
		return actor.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor actor) {
		return 1;
	}

	@Override
	public int[] getMobs() {
		return new int[]{3496};
	}

}
