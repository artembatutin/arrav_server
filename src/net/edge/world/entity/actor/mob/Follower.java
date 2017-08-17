package net.edge.world.entity.actor.mob;

import net.edge.world.locale.Position;
import net.edge.world.entity.actor.mob.Mob;

/**
 * The follower class which holds functionality for following types.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Follower extends Mob {
	
	/**
	 * Constructs a new {@link Follower} {@link Mob}.
	 * @param id       the identification for this follower.
	 * @param position the position this follower spawns at.
	 */
	public Follower(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public Mob create() {
		return new Follower(this.getId(), this.getPosition());
	}
}
