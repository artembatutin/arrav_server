package net.edge.content.skill.hunter.trap.mammal;

import net.edge.world.entity.actor.mob.DefaultMob;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobType;
import net.edge.world.locale.Position;

public class Mammal extends DefaultMob {

	private final MammalData data;
	private final MobType type;

	/**
	 * Creates a new {@link Mob}.
	 *
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	Mammal(int id, Position position, MammalData data) {
		super(id, position);
		this.type = MobType.HUNTING_MAMMAL;
		this.data = data;
	}

	@Override
	public Mob create() {
		return new Mammal(getId(), getPosition(), data);
	}

	@Override
	public MobType getMobType() {
		return type;
	}

	public MammalData getData() {
		return data;
	}
}
