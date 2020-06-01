package com.rageps.content.skill.slayer;

/**
 * Holds the data required for all the possible Slayer masters.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public enum SlayerMaster {
	TURAEL(8461, 1), SPRIA(8462, 1), MAZCHNA(8464, 20);
	
	/**
	 * The identifier for this slayer master id.
	 */
	private final int npcId;
	
	/**
	 * The slayer level required to access this slayer master.
	 */
	private final int requirement;
	
	/**
	 * Constructs a new {@link SlayerMaster}.
	 * @param npcId {@link #npcId}.
	 * @param requirement {@link #requirement}.
	 */
	SlayerMaster(int npcId, int requirement) {
		this.npcId = npcId;
		this.requirement = requirement;
	}
	
	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}
	
	/**
	 * @return the requirement
	 */
	public int getRequirement() {
		return requirement;
	}
}
