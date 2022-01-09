package com.rageps.net.refactor.packet.out.model.update;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.sync.seg.SynchronizationSegment;
import com.rageps.world.locale.Position;

import java.util.List;

/**
 * A {@link Packet} sent to the client to synchronize npcs with players.
 *
 * @author Major
 */
public final class NpcSynchronizationPacket extends Packet {

	/**
	 * The amount of local npcs.
	 */
	private final int localNpcs;

	/**
	 * The npc's position.
	 */
	private final Position position;

	/**
	 * A list of segments.
	 */
	private final List<SynchronizationSegment> segments;

	/**
	 * Creates a new {@link NpcSynchronizationPacket}.
	 *
	 * @param position The position of the {@link com.rageps.world.entity.actor.mob.Mob}.
	 * @param segments The list of segments.
	 * @param localNpcs The amount of local npcs.
	 */
	public NpcSynchronizationPacket(Position position, List<SynchronizationSegment> segments, int localNpcs) {
		this.position = position;
		this.segments = segments;
		this.localNpcs = localNpcs;
	}

	/**
	 * Gets the number of local npcs.
	 *
	 * @return The number of local npcs.
	 */
	public int getLocalNpcCount() {
		return localNpcs;
	}

	/**
	 * Gets the npc's position.
	 *
	 * @return The npc's position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the synchronization segments.
	 *
	 * @return The segments.
	 */
	public List<SynchronizationSegment> getSegments() {
		return segments;
	}

}