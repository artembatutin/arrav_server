package com.rageps.world.entity.sync.seg;


import com.rageps.world.entity.sync.block.SynchronizationBlock;
import com.rageps.world.entity.sync.block.SynchronizationBlockSet;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Direction;

/**
 * A segment contains a set of {@link SynchronizationBlock}s, {@link Direction}s (or teleport {@link Position}s) and any
 * other things required for the update of a single player.
 *
 * @author Graham
 */
public abstract class SynchronizationSegment {

	/**
	 * The {@link SynchronizationBlockSet}.
	 */
	private final SynchronizationBlockSet blockSet;

	/**
	 * Creates the segment.
	 *
	 * @param blockSet The block set.
	 */
	public SynchronizationSegment(SynchronizationBlockSet blockSet) {
		this.blockSet = blockSet;
	}

	/**
	 * Gets the block set.
	 *
	 * @return The block set.
	 */
	public final SynchronizationBlockSet getBlockSet() {
		return blockSet;
	}

	/**
	 * Gets the type of segment.
	 *
	 * @return The type of segment.
	 */
	public abstract SegmentType getType();

}