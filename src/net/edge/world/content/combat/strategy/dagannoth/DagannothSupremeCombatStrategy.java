package net.edge.world.content.combat.strategy.dagannoth;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Projectile;

import java.util.Arrays;
import java.util.Objects;

public final class DagannothSupremeCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public void incomingAttack(EntityNode character, EntityNode attacker, CombatSessionData data) {
		if(data.getType().equals(CombatType.RANGED) || data.getType().equals(CombatType.MAGIC)) {
			attacker.toPlayer().message("Your attacks are completely blocked...");
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
		
		World.submit(new Task(1, false) {
			@Override
			protected void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead())
					return;
				new Projectile(character, victim, 1937, 44, 4, 60, 43, 0).sendProjectile();
			}
		});
		return new CombatSessionData(character, victim, 1, CombatType.RANGED, true, 2);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 5;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{2881};
	}

}
