package com.rageps.content.minigame.barrows;

import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

/**
 * The npc which represents the barrow brother.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarrowBrother extends Mob {
	
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
	 * @param data {@link #data}.
	 * @param player the player this brother is spawned for.
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
	public Mob create() {
		return new BarrowBrother(data, player, this.getPosition());
	}
}
