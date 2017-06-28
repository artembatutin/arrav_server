package net.edge.content.minigame.pestcontrol.pest;

import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;

public class Torcher extends Pest {
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Torcher(int id, Position position, int instance) {
		super(id, position, instance);
	}
	
	@Override
	public void sequence(Npc knight) {
		//magic shoot over walls.
	}
	
}
