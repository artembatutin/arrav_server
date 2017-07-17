package net.edge.world.node.entity.update;

import net.edge.world.node.entity.npc.Npc;

/**
 * An {@link UpdateBlock} implementation specific to {@link Npc}s contained within an {@link UpdateBlockSet} and sent it.
 * @author lare96 <http://github.org/lare96>
 */
public abstract class NpcUpdateBlock extends UpdateBlock<Npc> {
	
	/**
	 * Creates a new {@link NpcUpdateBlock}.
	 * @param mask The bit mask for this update block.
	 * @param flag The update flag associated with this update block.
	 */
	public NpcUpdateBlock(int mask, UpdateFlag flag) {
		super(mask, flag);
	}
}