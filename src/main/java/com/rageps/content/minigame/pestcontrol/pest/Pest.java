package com.rageps.content.minigame.pestcontrol.pest;

import com.rageps.world.entity.actor.mob.DefaultMob;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.locale.Boundary;
import com.rageps.world.locale.Position;

public abstract class Pest extends DefaultMob {
	
	private static final Boundary pestBoundary = new Boundary(new Position(2623, 2558), new Position(2689, 2625));
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Pest(int id, Position position) {
		super(id, position);
		setAutoRetaliate(true);
		getMovementCoordinator().setBoundary(pestBoundary);
	}
	
	/**
	 * Sequencing this pest in the minigame.
	 * @param knight the pest minigame.
	 */
	public abstract void sequence(Mob knight);
	
	public abstract boolean aggressive();
	
	public boolean ranged() {
		return false;
	}
	
}
