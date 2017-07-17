package net.edge.content.minigame.barrows;

import net.edge.locale.Position;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

/**
 * The npc which represents the barrow brother.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarrowBrother extends Npc {
	
	/**
	 * The player for who the barrow brother will be spawned.
	 */
	private final Player player;
	
	/**
	 * The data of this barrow brother.
	 */
	private final BarrowsData data;
	
	/**
	 * Constructs a new {@link BarrowBrother}.
	 * @param data     {@link #data}.
	 * @param player   the player this brother is spawned for.
	 * @param position the position to spawn this brother at.
	 */
	BarrowBrother(BarrowsData data, Player player, Position position) {
		super(data.getNpcId(), position);
		this.data = data;
		this.setRespawn(false);
		this.setOwner(player);
		this.player = player;
	}
	
	/**
	 * @return the data.
	 */
	public BarrowsData getData() {
		return data;
	}
	
	@Override
	public Npc create() {
		return new BarrowBrother(data, player, this.getPosition());
	}
}
