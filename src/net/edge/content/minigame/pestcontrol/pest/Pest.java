package net.edge.content.minigame.pestcontrol.pest;

import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.impl.DefaultNpc;

public abstract class Pest extends DefaultNpc {
	
	private static final Boundary pestBoundary = new Boundary(new Position(2623, 2558), new Position(2689, 2625));
	
	/**
	 * Creates a new {@link Npc}.
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
	public abstract void sequence(Npc knight);
	
	public abstract boolean aggressive();
	
	public boolean ranged() {
		return false;
	}
	
}
