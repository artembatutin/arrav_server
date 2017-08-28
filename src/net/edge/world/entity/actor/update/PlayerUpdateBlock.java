package net.edge.world.entity.actor.update;

import net.edge.world.entity.actor.player.Player;

/**
 * An {@link UpdateBlock} implementation specific to {@link Player}s contained within an {@link UpdateManager} and sent.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class PlayerUpdateBlock extends UpdateBlock<Player> {

	/**
	 * Creates a new {@link PlayerUpdateBlock}.
	 *
	 * @param mask The bit mask for this update block.
	 * @param flag The update flag associated with this update block.
	 */
	public PlayerUpdateBlock(int mask, UpdateFlag flag) {
		super(mask, flag);
	}
}
