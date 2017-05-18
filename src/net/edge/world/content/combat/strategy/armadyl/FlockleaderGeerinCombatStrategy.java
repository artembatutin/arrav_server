package net.edge.world.content.combat.strategy.armadyl;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Projectile;
import net.edge.world.node.entity.npc.impl.gwd.KreeArra;

public final class FlockleaderGeerinCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
		World.submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead())
					return;
				new Projectile(character, victim, 1837, 44, 3, 43, 43, 0).sendProjectile();
			}
		});
		return new CombatSessionData(character, victim, 1, CombatType.RANGED, true);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 7;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{6225};
	}

}
