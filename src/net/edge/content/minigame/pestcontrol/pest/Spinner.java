package net.edge.content.minigame.pestcontrol.pest;

import net.edge.content.minigame.pestcontrol.PestPortal;
import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;

public class Spinner extends Pest {
	
	/**
	 * The portal protecting.
	 */
	private final PestPortal portal;
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param portal   the portal to defend.
	 */
	public Spinner(int id, PestPortal portal) {
		super(id, portal.getSpawn());
		this.portal = portal;
	}
	
	@Override
	public void sequence(Npc knight) {
		//heal portals and explode when portal is down.
	}
	
}
