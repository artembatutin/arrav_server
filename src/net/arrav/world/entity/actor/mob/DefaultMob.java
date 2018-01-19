package net.arrav.world.entity.actor.mob;

import net.arrav.world.locale.Position;

/**
 * Represents an single {@link Mob} with default npc behavior in the {@link World}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class DefaultMob extends Mob {
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public DefaultMob(int id, Position position) {
		super(id, position);
	}
	
	@Override
	public Mob create() {
		return new DefaultMob(this.getId(), this.getPosition());
	}
}
