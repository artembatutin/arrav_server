package net.edge.world.node.actor.npc.impl;

import net.edge.locale.Position;
import net.edge.world.node.actor.npc.Npc;

/**
 * The follower class which holds functionality for following types.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Follower extends Npc {
	
	/**
	 * Constructs a new {@link Follower} {@link Npc}.
	 * @param id       the identification for this follower.
	 * @param position the position this follower spawns at.
	 */
	public Follower(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public Npc create() {
		return new Follower(this.getId(), this.getPosition());
	}
}
