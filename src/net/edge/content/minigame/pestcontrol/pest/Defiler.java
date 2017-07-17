package net.edge.content.minigame.pestcontrol.pest;

import net.edge.content.combat.CombatType;
import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.node.actor.npc.Npc;

import java.util.Optional;

public class Defiler extends Pest {
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Defiler(int id, Position position) {
		super(id, position);
		getAttr().get("master_archery").set(true);
		getCombatBuilder().setCombatType(CombatType.RANGED);
	}
	
	@Override
	public void sequence(Npc knight) {
		if(getCombatBuilder().getAggressor() != null && getCombatBuilder().getAggressor().isPlayer()) {
			getCombatBuilder().attack(getCombatBuilder().getAggressor());
		} else if(!getPosition().withinDistance(knight.getPosition(), 15) && !getCombatBuilder().isAttacking()) {
			Position delta = Position.delta(getPosition(), knight.getPosition());
			int x = RandomUtils.inclusive(delta.getX() < 0 ? -delta.getX() : delta.getX());
			int y = RandomUtils.inclusive(delta.getY() < 0 ? -delta.getY() : delta.getY());
			Position move = getPosition().move((delta.getX() < 0 ? -x : x) + RandomUtils.inclusive(3), (delta.getY() < 0 ? -y : y) + RandomUtils.inclusive(3));
			Optional<Position> destination = World.getTraversalMap().getRandomTraversableTile(move, size());
			destination.ifPresent(d -> getMovementQueue().smartWalk(d));
		}
	}
	
	@Override
	public boolean aggressive() {
		return true;
	}
	
	@Override
	public boolean ranged() {
		return true;
	}
}
