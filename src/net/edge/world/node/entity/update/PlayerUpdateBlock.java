package net.edge.world.node.entity.update;

import net.edge.world.node.entity.player.Player;

/**
 * An {@link UpdateBlock} implementation specific to {@link Player}s contained within an {@link UpdateBlockSet} and sent.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class PlayerUpdateBlock extends UpdateBlock<Player> {
	
	/**
	 * Creates a new {@link PlayerUpdateBlock}.
	 * @param mask The bit mask for this update block.
	 * @param flag The update flag associated with this update block.
	 */
	public PlayerUpdateBlock(int mask, UpdateFlag flag) {
		super(mask, flag);
	}
}
