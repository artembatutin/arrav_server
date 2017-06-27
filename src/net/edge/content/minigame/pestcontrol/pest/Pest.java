package net.edge.content.minigame.pestcontrol.pest;

import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.impl.DefaultNpc;

public abstract class Pest extends DefaultNpc {
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Pest(int id, Position position, int instance) {
		super(id, position);
		setInstance(instance);
		setAutoRetaliate(true);
	}
	
	/**
	 * Sequencing this pest in the minigame.
	 * @param knight the pest minigame.
	 */
	public abstract void sequence(Npc knight);
	
}
