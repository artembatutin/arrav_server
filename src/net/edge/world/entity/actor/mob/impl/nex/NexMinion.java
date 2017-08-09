package net.edge.world.entity.actor.mob.impl.nex;

import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.NexStrategy;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The class which represents a nex minion.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NexMinion extends Mob {
	
	/**
	 * Constructs a new {@link NexMinion}.
	 */
	public NexMinion(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public Mob create() {
		return new NexMinion();
	}
	
}
