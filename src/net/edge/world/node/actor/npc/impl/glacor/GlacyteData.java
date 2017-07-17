package net.edge.world.node.actor.npc.impl.glacor;

import com.google.common.collect.ImmutableSet;

/**
 * The enumerated type whose elements represent a set of constants used to define
 * the sorts of glacytes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum GlacyteData {
	UNSTABLE(14302),
	SAPPING(14303),
	ENDURING(14304);
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<GlacyteData> VALUES = ImmutableSet.copyOf(values());
	
	/**
	 * The npc id for this glacyte.
	 */
	private final int npcId;

	/**
	 * Constructs a new {@link GlacyteData}.
	 * @param npcId {@link #npcId}.
	 */
	private GlacyteData(int npcId) {
		this.npcId = npcId;
	}
	
	public int getNpcId() {
		return npcId;
	}
}
