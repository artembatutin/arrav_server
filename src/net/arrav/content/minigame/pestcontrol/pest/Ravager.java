package net.arrav.content.minigame.pestcontrol.pest;

import net.arrav.content.minigame.pestcontrol.PestControlMinigame;
import net.arrav.content.minigame.pestcontrol.defence.PestGate;
import net.arrav.world.Animation;
import net.arrav.world.Direction;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

import java.util.Optional;

public class Ravager extends Pest {
	
	/**
	 * The nearest gate attacking.
	 */
	private PestGate gate;
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Ravager(int id, Position position) {
		super(id, position);
		setAutoRetaliate(false);
	}
	
	@Override
	public void sequence(Mob knight) {
		//attacking gates and barricades first. ignoring player attacks
		if(gate == null && getPosition() != null) {
			gate = PestControlMinigame.getNearestGate(getPosition());
		}
		if(gate != null) {
			if(gate.destroyed()) {
				if(!getPosition().withinDistance(knight.getPosition(), 5) && getCombat().getLastDefender() != null) {
					Optional<Position> destination = TraversalMap.getRandomTraversableTile(getCombat().getLastDefender().getPosition(), size());
					destination.ifPresent(d -> getMovementQueue().smartWalk(d));
				}
			} else {
				if(getMovementQueue().isMovementDone())
					getMovementQueue().smartWalk(gate.getPos().move(Direction.fromDeltas(Position.delta(gate.getPos(), getPosition()))));
				attackGate();
			}
		}
	}
	
	@Override
	public boolean aggressive() {
		return false;
	}
	
	private void attackGate() {
		if(gate != null && !gate.destroyed() && getPosition().withinDistance(gate.getPos(), 1)) {
			animation(new Animation(getDefinition().getAttackAnimation()));
			facePosition(gate.getPos());
			gate.damage();
		}
	}
	
}
