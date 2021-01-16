package com.rageps.world.entity.sync.seg;


import com.rageps.world.entity.sync.block.SynchronizationBlockSet;
import com.rageps.world.locale.Position;

/**
 * A {@link SynchronizationSegment} where the mob is teleported to a new location.
 *
 * @author Graham
 */
public final class TeleportSegment extends SynchronizationSegment {

	/**
	 * The destination.
	 */
	private final Position destination;

	/**
	 * Creates the teleport segment.
	 *
	 * @param blockSet The block set.
	 * @param destination The destination.
	 */
	public TeleportSegment(SynchronizationBlockSet blockSet, Position destination) {
		super(blockSet);
		this.destination = destination;
	}

	/**
	 * Gets the destination.
	 *
	 * @return The destination.
	 */
	public Position getDestination() {
		return destination;
	}

	@Override
	public SegmentType getType() {
		return SegmentType.TELEPORT;
	}

}