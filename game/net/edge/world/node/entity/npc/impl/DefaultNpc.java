package net.edge.world.node.entity.npc.impl;

import net.edge.World;
import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;

/**
 * Represents an single {@link Npc} with default npc behavior in the {@link World}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class DefaultNpc extends Npc {
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public DefaultNpc(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public Npc create() {
		return new DefaultNpc(this.getId(), this.getPosition());
	}
}
