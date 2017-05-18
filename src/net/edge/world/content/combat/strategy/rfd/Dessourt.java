package net.edge.world.content.combat.strategy.rfd;

import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;

import java.util.Arrays;

public final class Dessourt implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		victim.animation(new Animation(3508));
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, false);
	}
	
	@Override
	public void incomingAttack(EntityNode character, EntityNode victim, CombatSessionData data) {
		if(data.getType() == CombatType.MELEE) {
			Arrays.stream(data.getHits()).forEach(hit -> hit.setDamage(hit.getDamage() / 2));
			character.graphic(new Graphic(550));
			character.forceChat("Hssssssssssss");
		}
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 1;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{3496};
	}

}
