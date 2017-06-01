package net.edge.content.minigame.barrows;

import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;

/**
 * The npc which represents the barrow brother.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarrowBrother extends Npc {
	
	/**
	 * The data of this barrow brother.
	 */
	private final BarrowsData data;
	
	/**
	 * Constructs a new {@link BarrowBrother}.
	 * @param data     {@link #data}.
	 * @param username the player this brother is spawned for.
	 * @param position the position to spawn this brother at.
	 */
	BarrowBrother(BarrowsData data, String username, Position position) {
		super(data.getNpcId(), position);
		this.data = data;
		this.setRespawn(false);
		this.setSpawnedFor(username);
	}
	
	/**
	 * @return the data.
	 */
	public BarrowsData getData() {
		return data;
	}
	
	@Override
	public Npc create() {
		return new BarrowBrother(data, this.getSpawnedFor(), this.getPosition());
	}
}
