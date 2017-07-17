package net.edge.content.minigame.pestcontrol.pest;

import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.impl.DefaultMob;

public abstract class Pest extends DefaultMob {
	
	private static final Boundary pestBoundary = new Boundary(new Position(2623, 2558), new Position(2689, 2625));
	
	/**
	 * Creates a new {@link Mob}.
	 * @param id       the identification for this NPC.
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
