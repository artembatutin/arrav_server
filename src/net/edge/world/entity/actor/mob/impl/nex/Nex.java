package net.edge.world.entity.actor.mob.impl.nex;

import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.NexStrategy;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The class which represents the nex boss in chamber.
 * @author Artem Batutin <artembatutin@gmail.com>
 * https://www.youtube.com/watch?v=_cJxB5j1HR8
 */
public final class Nex extends Mob {
	
	/**
	 * The nex chamber stage.
	 */
	private int stage;
	
	/**
	 * Constructs a new {@link Nex}.
	 */
	public Nex() {
		super(9177, new Position(3386, 3517));
		setStrategy(Optional.of(new NexStrategy(this)));
		stage = 9177;
	}
	
	@Override
	public Mob create() {
		return new Nex();
	}
	
}
