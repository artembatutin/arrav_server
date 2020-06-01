package com.rageps.world.entity.actor.update;

import com.rageps.world.entity.actor.mob.Mob;

/**
 * An {@link UpdateBlock} implementation specific to {@link Mob}s contained within an {@link UpdateBlockSet} and sent it.
 * @author lare96 <http://github.org/lare96>
 */
public abstract class MobUpdateBlock extends UpdateBlock<Mob> {
	
	/**
	 * Creates a new {@link MobUpdateBlock}.
	 * @param mask The bit mask for this update block.
	 * @param flag The update flag associated with this update block.
	 */
	public MobUpdateBlock(int mask, UpdateFlag flag) {
		super(mask, flag);
	}
}