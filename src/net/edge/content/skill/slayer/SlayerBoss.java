package net.edge.content.skill.slayer;

import net.edge.world.entity.item.Item;

/**
 * Represents a slayer boss task for the slayer skill
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum SlayerBoss {
	
	ABYSSAL_ORPHAN("ABYSSAL DEMONS", 500, new Item(3904), 3167),
	JAD_JADIKU("TZTOK_JAD", 20, new Item(3906), 3168),
	TORAM("TORMENTED_DEMONS", 100, new Item(3908), 3169),
	WYRMY("WILDY_WYRM", 150, new Item(3910), 3170),
	KRAA("KREE_ARRA", 20, new Item(3912), 3177),
	GRARY("GENERAL_GRARRDOR", 20, new Item(3914), 3178),
	//TRSUTSY("KRIL_TSUTSAROTH", 20, new Item(3916), 3179),
	ZILZY("COMMANDER_ZILYANA", 20, new Item(3918), 3180);
	
	/**
	 * The identifier for this slayer key.
	 */
	private final String key;
	
	/**
	 * The amount of times you need to kill this slayer key.
	 */
	private final int amount;
	
	/**
	 * The reward for killing this boss.
	 */
	private final Item reward;
	
	/**
	 * The minion id.
	 */
	private final int minion;
	
	/**
	 * Constructs a new {@link SlayerBoss}.
	 */
	SlayerBoss(String key, int amount, Item reward, int minion) {
		this.key = key;
		this.amount = amount;
		this.reward = reward;
		this.minion = minion;
	}
	
	public String getKey() {
		return key;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public Item getReward() {
		return reward;
	}
	
	public int getMinion() {
		return minion;
	}
	
	public void event() {
		for(SlayerBoss boss : SlayerBoss.values()) {
			Slayer.SLAYER_BOSSES.add(boss);
			
		}
		
	}
}
