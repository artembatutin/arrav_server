package net.edge.world.entity.actor.mob.drop;

import java.util.Optional;

/**
 * The enumerated type containing {@link Drop}s common among multiple
 * {@link DropTable}s.
 * @author lare96 <http://github.org/lare96>
 */
public enum ItemCache {
	
	/**
	 * Contains the herb seeds.
	 */
	HERB_SEEDS, //-1
	
	/**
	 * Contains the flower seeds.
	 */
	FLOWER_SEEDS, //-2
	
	/**
	 * Contains the allotment seeds.
	 */
	ALLOTMENT_SEEDS, //-3
	
	/**
	 * Contains the summoning charms.
	 */
	CHARMS, //-4
	
	/**
	 * Contains only the basic elemental air, water, earth...
	 */
	LOW_RUNES, //-5
	
	/**
	 * Contains high quantities of basic elemental runes, astral runes, and low
	 * quantities of higher tier runes such as death, blood, law, nature, and
	 * soul.
	 */
	MED_RUNES, //-6
	
	/**
	 * Contains extremely high quantities of all runes excluding low-level
	 * combat spell runes such as body and mind.
	 */
	HIGH_RUNES, //-7
	
	/**
	 * Contains only low level herbs such as Guam, Marrentill, Tarromin,
	 * Harralander, and Ranarr.
	 */
	LOW_HERBS, //-8
	
	/**
	 * Contains medium level herbs such as Ranarr, Toadflax, Irit, Avantoe, and
	 * Kwuarm.
	 */
	MED_HERBS, //-9
	
	/**
	 * Contains high level herbs such as Snapdragon, Cadantine, Lantadyme, Dwarf
	 * Weed, and Torstol. Also contains the herbs from the medium tier. All of
	 * the herbs in this tier are noted.
	 */
	HIGH_HERBS, //-10
	
	/**
	 * Contains ores from gold to adamantine.
	 */
	MED_MINERALS, //-11
	
	/**
	 * Contains only noted sapphire and emerald gems in low quantities.
	 */
	LOW_GEMS, //-12
	
	/**
	 * Contains the gems from the lower tier as well as ruby and diamond noted
	 * gems in higher quantities.
	 */
	MED_GEMS, //-13
	
	/**
	 * Contains the noted gems from the lower tiers as well as dragonstone and
	 * onyx gems in the highest possible quantities.
	 */
	HIGH_GEMS, //-14
	
	/**
	 * The barrows items that are the main rare items from the barrow brothers
	 * minigame.
	 */
	BARROWS, //-15
	
	/**
	 * Caskets items giving coins on clicking.
	 */
	CASKETS //-16
	;
	
	/**
	 * Custom short negative id.
	 */
	private final int ord;
	
	/**
	 * Sets up the {@link #ord}.
	 */
	ItemCache() {
		ord = 65535 - ordinal();
	}
	
	/**
	 * Finds a item cache if possible.
	 * @param num id
	 * @return item cache
	 */
	public static Optional<ItemCache> get(int num) {
		for(ItemCache cache : values()) {
			if(num == cache.ord)
				return Optional.of(cache);
		}
		return Optional.empty();
	}
	
}