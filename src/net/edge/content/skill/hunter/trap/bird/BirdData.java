package net.edge.content.skill.hunter.trap.bird;

import net.edge.world.entity.item.Item;

/**
 * The enumerated type whose elements represent a set of constants
 * used for bird snaring.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum BirdData {
	CRIMSON_SWIFT(5073, 19180, 1, 24, 526, 10088, 9978),
	GOLDEN_WARBLER(5075, 19184, 5, 37, 526, 10090, 9978),
	COPPER_LONGTAIL(5076, 19186, 9, 51, 526, 10091, 9978),
	CERULEAN_TWITCH(5074, 19182, 11, 74.5, 526, 10089, 9978),
	TROPICAL_WAGTAIL(5072, 19178, 19, 95, 526, 10087, 9978);
	
	/**
	 * The npc id for this bird.
	 */
	final int npcId;
	
	/**
	 * The object id for the catched bird.
	 */
	public final int objectId;
	
	/**
	 * The requirement for this bird.
	 */
	final int requirement;
	
	/**
	 * The experience gained for this bird.
	 */
	final double experience;
	
	/**
	 * The reward obtained for this bird.
	 */
	final Item[] reward;
	
	/**
	 * Constructs a new {@link BirdData}.
	 * @param npcId       {@link #npcId}.
	 * @param objectId    {@link #objectId}
	 * @param requirement {@link #requirement}.
	 * @param experience  {@link #experience}.
	 * @param reward      {@link #reward}.
	 */
	BirdData(int npcId, int objectId, int requirement, double experience, int... reward) {
		this.npcId = npcId;
		this.objectId = objectId;
		this.requirement = requirement;
		this.experience = experience;
		this.reward = Item.convert(reward);
	}
	
	/**
	 * @return the npc id.
	 */
	public int getNpcId() {
		return npcId;
	}
	
	public static void action() {
//		for(BirdData data : BirdData.values()) {
//			Mob.CUSTOM_MOBS.put(data.getNpcId(), s -> new Bird(data.getNpcId(), s, data)); TODO: birds hehe
//		}
	}
	
}
