package net.arrav.content.skill.hunter.trap.bird;

import net.arrav.world.entity.actor.mob.DefaultMob;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.MobType;
import net.arrav.world.locale.Position;

public class Bird extends DefaultMob {

	private final BirdData data;
	private final MobType type;

	/**
	 * Creates a new {@link Mob}.
	 * @param id the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	Bird(int id, Position position, BirdData data) {
		super(id, position);
		this.type = MobType.HUNTING_BIRD;
		this.data = data;
	}

	@Override
	public Mob create() {
		return new Bird(getId(), getPosition(), data);
	}

	@Override
	public MobType getMobType() {
		return type;
	}

	public BirdData getData() {
		return data;
	}
}
