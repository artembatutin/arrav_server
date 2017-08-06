package net.edge.content.skill.hunter.trap.mammal;

import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.item.Item;

/**
 * The enumerated type whose elements represent a set of constants
 * used for box trapping.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum MammalData {
	GECKO(7289, 27, 100, 12488),
	MONKEY(7228, 27, 100, 12201),
	RACCOON(6997, 27, 100, 12199),
	FERRET(5081, 27, 115, 10092),
	CHINCHOMPA(5079, 53, 198.25, 10033),
	CARNIVOROUS_CHINCHOMPA(5080, 63, 265, 10034),
	PAWYA(7012, 66, 400, 12535),
	GRENWALL(7010, 77, 415, 12539);
	
	/**
	 * The npc id for this box trap.
	 */
	final int npcId;
	
	/**
	 * The requirement for this box trap.
	 */
	final int requirement;
	
	/**
	 * The experience gained for this box trap.
	 */
	final double experience;
	
	/**
	 * The reward obtained for this box trap.
	 */
	final Item[] reward;
	
	/**
	 * Constructs a new {@link MammalData}.
	 * @param npcId       {@link #npcId}.
	 * @param requirement {@link #requirement}.
	 * @param experience  {@link #experience}.
	 * @param reward      {@link #reward}.
	 */
	MammalData(int npcId, int requirement, double experience, int... reward) {
		this.npcId = npcId;
		this.requirement = requirement;
		this.experience = experience;
		this.reward = Item.convert(reward);
	}
	
	public static void action() {
		for(MammalData data : MammalData.values()) {
			Mob.CUSTOM_MOBS.put(data.npcId, s -> new Mammal(data.npcId, s, data));
		}
	}
	
}
