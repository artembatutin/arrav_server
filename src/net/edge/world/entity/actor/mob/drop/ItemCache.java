package net.edge.world.entity.actor.mob.drop;

/**
 * The enumerated type containing {@link Drop}s common among multiple
 * {@link DropTable}s.
 * @author lare96 <http://github.org/lare96>
 */
public enum ItemCache {
	
	/**
	 * Contains the herb seeds.
	 */
	HERB_SEEDS,
	
	/**
	 * Contains the flower seeds.
	 */
	FLOWER_SEEDS,
	
	/**
	 * Contains the allotment seeds.
	 */
	ALLOTMENT_SEEDS,
	
	/**
	 * Contains the summoning charms.
	 */
	CHARMS,
	
	/**
	 * Contains only the basic elemental air, water, earth...
	 */
	LOW_RUNES,
	
	/**
	 * Contains high quantities of basic elemental runes, astral runes, and low
	 * quantities of higher tier runes such as death, blood, law, nature, and
	 * soul.
	 */
	MED_RUNES,
	
	/**
	 * Contains extremely high quantities of all runes excluding low-level
	 * combat spell runes such as body and mind.
	 */
	HIGH_RUNES,
	
	/**
	 * Contains only low level herbs such as Guam, Marrentill, Tarromin,
	 * Harralander, and Ranarr.
	 */
	LOW_HERBS,
	
	/**
	 * Contains medium level herbs such as Ranarr, Toadflax, Irit, Avantoe, and
	 * Kwuarm.
	 */
	MED_HERBS,
	
	/**
	 * Contains high level herbs such as Snapdragon, Cadantine, Lantadyme, Dwarf
	 * Weed, and Torstol. Also contains the herbs from the medium tier. All of
	 * the herbs in this tier are noted.
	 */
	HIGH_HERBS,
	
	/**
	 * Contains ores from gold to adamantine.
	 */
	MED_MINERALS,
	
	/**
	 * Contains only noted sapphire and emerald gems in low quantities.
	 */
	LOW_GEMS,
	
	/**
	 * Contains the gems from the lower tier as well as ruby and diamond noted
	 * gems in higher quantities.
	 */
	MED_GEMS,
	
	/**
	 * Contains the noted gems from the lower tiers as well as dragonstone and
	 * onyx gems in the highest possible quantities.
	 */
	HIGH_GEMS,
	
	/**
	 * The barrows items that are the main rare items from the barrow brothers
	 * minigame.
	 */
	BARROWS,
	
	/**
	 * Caskets items giving coins on clicking.
	 */
	CASKETS
	;
	
}