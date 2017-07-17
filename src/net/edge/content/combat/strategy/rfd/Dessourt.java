package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;

import java.util.Arrays;

public final class Dessourt implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		victim.animation(new Animation(3508));
		return new CombatHit(character, victim, 1, CombatType.MELEE, false);
	}
	
	@Override
	public void incomingAttack(Actor character, Actor victim, CombatHit data) {
		if(data.getType() == CombatType.MELEE) {
			Arrays.stream(data.getHits()).forEach(hit -> hit.setDamage(hit.getDamage() / 2));
			character.graphic(new Graphic(550));
			character.forceChat("Hssssssssssss");
		}
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Actor character) {
		return 1;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{3496};
	}

}
