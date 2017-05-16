package net.edge.world.content.skill.summoning.specials;

import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.model.node.item.Item;

/**
 * An enumeration of all {@link Summoning} pouches.
 */
public enum SummoningData {
	
	/**
	 * Represents a spirit wolf familiar.
	 */
	SPIRIT_WOLF(0, 12047, 1, 4.8, 6829, 0.1, 1, Charm.GOLD, 6, new Item(12158), new Item(12155), new Item(2859), new Item(12183, 7)),
	
	/**
	 * Represents a dreadfowl familiar.
	 */
	DREADFOWL(1, 12043, 4, 9.3, 6825, 0.1, 1, Charm.GOLD, 4, new Item(12158), new Item(12155), new Item(2138), new Item(12183, 8)),
	
	/**
	 * Represents a spirit spider familiar.
	 */
	SPIRIT_SPIDER(2, 12059, 10, 12.6, 6841, 0.2, 2, Charm.GOLD, 15, new Item(12158), new Item(12155), new Item(6291), new Item(12183, 8)),
	
	/**
	 * Represents a thorny snail familiar.
	 */
	THORNY_SNAIL(3, 12019, 13, 12.6, 6806, 0.2, 2, Charm.GOLD, 16, new Item(12158), new Item(12155), new Item(3363), new Item(12183, 9)),
	
	/**
	 * Represents a granite crab familiar.
	 */
	GRANITE_CRAB(4, 12009, 16, 31.6, 6796, 0.2, 2, Charm.CRIMSON, 18, new Item(12158), new Item(12155), new Item(440), new Item(12183, 7)),
	
	/**
	 * Represents a spirit mosquito familiar.
	 */
	SPIRIT_MOSQUITO(5,12778, 17, 46.5, 7331, 0.5, 2, Charm.GOLD, 12, new Item(12158), new Item(12155), new Item(6319), new Item(12183, 1)),
	
	/**
	 * Represents a desrrt wyrm familiar.
	 */
	DESERT_WYRM(6, 12049, 18, 31.2, 6831, 0.4, 1, Charm.GREEN, 19, new Item(12159), new Item(12155), new Item(1783), new Item(12183, 45)),
	
	/**
	 * Represents a spirit scorpion familiar.
	 */
	SPIRIT_SCORPION(7, 12055, 19, 83.2, 6837, 0.9, 2, Charm.CRIMSON, 17, new Item(12160), new Item(12155), new Item(3095), new Item(12183, 57)),
	
	/**
	 * Represents a spirit tz-kih familiar.
	 */
	SPIRIT_TZ_KIH(8, 12808, 22, 96.8, 7361, 1.1, 3, Charm.CRIMSON, 18, new Item(12160), new Item(12168), new Item(12155), new Item(12183, 64)),
	
	/**
	 * Represents an albino rat familiar.
	 */
	ALBINO_RAT(9, 12067, 23, 202.4, 6847, 2.3, 1, Charm.BLUE, 22, new Item(12163), new Item(12155), new Item(2134), new Item(12183, 75)),
	
	/**
	 * Represents a spirit kalphite familiar.
	 */
	SPIRIT_KALPHITE(10, 12063, 25, 220, 6994, 2.5, 3, Charm.BLUE, 22, new Item(12163), new Item(12155), new Item(3138), new Item(12183, 51)),
	
	/**
	 * Represents a compost mound familiar.
	 */
	COMPOST_MOUND(11, 12091, 28, 49.8, 6871, 0.6, 6, Charm.GREEN, 24, new Item(12159), new Item(12155), new Item(6032), new Item(12183, 47)),
	
	/**
	 * Represents a giant chinchompa familiar.
	 */
	GIANT_CHINCHOMPA(12, 12800, 29, 255.2, 7353, 2.9, 1, Charm.BLUE, 31, new Item(12163), new Item(12155), new Item(10033), new Item(12183, 84)),
	
	/**
	 * Represents a vampire bat familiar.
	 */
	VAMPYRE_BAT(13, 12053, 31, 136, 6835, 1.5, 4, Charm.CRIMSON, 33, new Item(12160), new Item(12155), new Item(3325), new Item(12183, 81)),
	
	/**
	 * Represents a honey badger familiar.
	 */
	HONEY_BADGER(14, 12065, 32, 140.8, 6845, 1.6, 4, Charm.CRIMSON, 25, new Item(12160), new Item(12155), new Item(12156), new Item(12183, 84)),
	
	/**
	 * Represents a beaver familiar.
	 */
	BEAVER(15, 12021, 33, 57.6, 6808, 0.7, 4, Charm.GREEN, 27,new Item(12159), new Item(12155), new Item(1519), new Item(12183, 72)),
	
	/**
	 * Represents a void ravager familiar.
	 */
	VOID_RAVAGER(16, 12818, 34, 59.6, 7370, 0.7, 4, Charm.GREEN, 27, new Item(12159), new Item(12164), new Item(12155), new Item(12183, 74)),
	
	/**
	 * Represents a void spinner familiar.
	 */
	VOID_SPINNER(17, 12780, 34, 59.6, 7333, 0.7, 4, Charm.BLUE, 27, new Item(12163), new Item(12166), new Item(12155), new Item(12183, 74)),
	
	/**
	 * Represents a void torcher familiar.
	 */
	VOID_TORCHER(18, 12798, 34, 59.6, 7351, 0.7, 4, Charm.BLUE, 94, new Item(12163), new Item(12167), new Item(12155), new Item(12183, 74)),
	
	/**
	 * Represents a void shifter familiar.
	 */
	VOID_SHIFTER(19, 12814, 34, 59.6, 7367, 0.7, 4, Charm.BLUE, 94, new Item(12163), new Item(12165), new Item(12155), new Item(12183, 74)),
	
	/**
	 * Represents a bronze minotaur familiar.
	 */
	BRONZE_MINOTAUR(64, 12073, 36, 316.8, 6853, 3.6, 3, Charm.BLUE, 30, new Item(12163), new Item(12155), new Item(2349), new Item(12183, 102)),
	
	/**
	 * Represents an iron minotaur familiar.
	 */
	IRON_MINOTAUR(65, 12075, 46, 404.8, 6855, 4.6, 9, Charm.BLUE, 37, new Item(12163), new Item(12155), new Item(2351), new Item(12183, 125)),
	
	/**
	 * Represents a steel minotaur familiar.
	 */
	STEEL_MINOTAUR(66, 12077, 56, 492.8, 6857, 5.6, 9, Charm.BLUE, 46, new Item(12163), new Item(12155), new Item(2353), new Item(12183, 141)),
	
	/**
	 * Represents a mithril minotaur familiar.
	 */
	MITHRIL_MINOTAUR(67, 12079, 66, 580.8, 6859, 6.6, 9, Charm.BLUE, 55, new Item(12163), new Item(12155), new Item(2359), new Item(12183, 152)),
	
	/**
	 * Represents an adamant minotaur familiar.
	 */
	ADAMANT_MINOTAUR(68, 12081, 76, 668.8, 6861, 7.6, 9, Charm.BLUE, 66, new Item(12163), new Item(12155), new Item(2361), new Item(12183, 144)),
	
	/**
	 * Represents a rune minotaur familiar.
	 */
	RUNE_MINOTAUR(69, 12083, 86, 756.8, 6863, 8.6, 9, Charm.BLUE, 151, new Item(12163), new Item(12155), new Item(2363), new Item(12183, 1)),
	
	/**
	 * Represents a bull ant familiar.
	 */
	BULL_ANT(20, 12087, 40, 52.8, 6867, 0.6, 5, Charm.GOLD, 30, new Item(12158), new Item(12155), new Item(6010), new Item(12183, 11)),
	
	/**
	 * Represents a macaw familiar.
	 */
	MACAW(21, 12071, 41, 72.4, 6851, 0.8, 5, Charm.GREEN, 31, new Item(12159), new Item(12155), new Item(249), new Item(12183, 78)),
	
	/**
	 * Represents an evil turnip familiar.
	 */
	EVIL_TURNIP(22, 12051, 42, 184.8, 6833, 2.1, 5, Charm.CRIMSON, 30, new Item(12160), new Item(12155), new Item(12153), new Item(12183, 104)),
	
	/**
	 * Represents a spirit cockatrice familiar.
	 */
	SPIRIT_COCKATRICE(23, 12095, 43, 75.2, 6875, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12109), new Item(12183, 88)),
	
	/**
	 * Represents a spirit guthatrice familiar.
	 */
	SPIRIT_GUTHATRICE(24, 12097, 43, 75.2, 6877, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12111), new Item(12183, 88)),
	
	/**
	 * Represents a spirit saratrice familiar.
	 */
	SPIRIT_SARATRICE(25, 12099, 43, 75.2, 6879, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12113), new Item(12183, 88)),
	
	/**
	 * Represents a spirit zamatrice familiar.
	 */
	SPIRIT_ZAMATRICE(26, 12101, 43, 75.2, 6881, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12115), new Item(12183, 88)),
	
	/**
	 * Represents a spirit pengatrice familiar.
	 */
	SPIRIT_PENGATRICE(27, 12103, 43, 75.2, 6883, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12117), new Item(12183, 88)),
	
	/**
	 * Represents a coraxatrice familiar.
	 */
	SPIRIT_CORAXATRICE(28, 12105, 43, 75.2, 6885, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12119), new Item(12183, 88)),
	
	/**
	 * Represents a vulatrice familiar.
	 */
	SPIRIT_VULATRICE(29, 12107, 43, 75.2, 6887, 0.9, 5, Charm.GREEN, 36, new Item(12159), new Item(12155), new Item(12121), new Item(12183, 88));
	
	/**
	 * Represents a pyrelord familiar.
	 */
	//PYRELORD_POUCH(30, 12816, 46, 202.4, 7377, 2.3, 5, new Item(12160), new Item(12155), new Item(590), new Item(12183, 111)),
	
	/**
	 * Represents a magpie familiar.
	 */
	//MAGPIE_POUCH(31, 12041, 47, 83.2, 6824, 0.9, 5, new Item(12159), new Item(12155), new Item(1635), new Item(12183, 88)),
	
	/**
	 * Represents a bloated leech familiar.
	 */
	//BLOATED_LEECH_POUCH(32, 12061, 49, 215.2, 6843, 2.4, 5, new Item(12160), new Item(12155), new Item(2132), new Item(12183, 117)),
	
	/**
	 * Represents a spirit terrorbird familiar.
	 */
	//SPIRIT_TERRORBIRD_POUCH(33, 12007, 52, 68.4, 6794, 0.8, 6, new Item(12158), new Item(12155), new Item(9978), new Item(12183, 12)),
	
	/**
	 * Represents an abyssal parasite familiar.
	 */
	//ABYSSAL_PARASITE_POUCH(34, 12035, 54, 94.8, 6818, 1.1, 6, new Item(12159), new Item(12155), new Item(12161), new Item(12183, 106)),
	
	/**
	 * Represents a spirit jelly familiar.
	 */
	//SPIRIT_JELLY_POUCH(35, 12027, 55, 484, 6992, 5.5, 6, new Item(12163), new Item(12155), new Item(1937), new Item(12183, 151)),
	
	/**
	 * Represents an ibis familiar.
	 */
	//IBIS_POUCH(36, 12531, 56, 98.8, 6991, 1.1, 6, new Item(12159), new Item(12155), new Item(311), new Item(12183, 109)),
	
	/**
	 * Represents a spirit kyatt familiar.
	 */
	//SPIRIT_KYATT_POUCH(37, 12812, 57, 501.6, 7365, 5.7, 6, new Item(12163), new Item(12155), new Item(10103), new Item(12183, 153)),
	
	/**
	 * Represents a spirit larupia familiar.
	 */
	//SPIRIT_LARUPIA_POUCH(38, 12784, 57, 501.6, 7337, 5.7, 6, new Item(12163), new Item(12155), new Item(10095), new Item(12183, 155)),
	
	/**
	 * Represents a spirit graahk familiar.
	 */
	//SPIRIT_GRAAHK_POUCH(39, 12810, 57, 501.6, 7363, 5.7, 6, new Item(12163), new Item(12155), new Item(10099), new Item(12183, 154)),
	
	/**
	 * Represents a karamthulhu overlord familiar.
	 */
	//KARAMTHULHU_POUCH(40, 12023, 58, 510.4, 6809, 5.8, 6, new Item(12163), new Item(12155), new Item(6667), new Item(12183, 144)),
	
	/**
	 * Represents a smoke devil familiar.
	 */
	//SMOKE_DEVIL_POUCH(41, 12085, 61, 268, 6865, 3, 7, new Item(12160), new Item(12155), new Item(9736), new Item(12183, 141)),
	
	/**
	 * Represents an abyssal lurker familiar.
	 */
	//ABYSSAL_LUKRER(42, 12037, 62, 109.6, 6820, 1.9, 9, new Item(12159), new Item(12155), new Item(12161), new Item(12183, 119)),
	
	/**
	 * Represents a spirit cobra familiar.
	 */
	//SPIRIT_COBRA_POUCH(43, 12015, 63, 276.8, 6802, 3.1, 6, new Item(12160), new Item(12155), new Item(6287), new Item(12183, 116)),
	
	/**
	 * Represents a stranger plant familiar.
	 */
	//STRANGER_PLANT_POUCH(44, 12045, 64, 281.6, 6827, 3.2, 6, new Item(12160), new Item(12155), new Item(8431), new Item(12183, 128)),
	
	/**
	 * Represents a barker toad familiar.
	 */
	//BARKER_TOAD_POUCH(45, 12123, 66, 87, 6889, 1, 7, new Item(12158), new Item(12155), new Item(2150), new Item(12183, 11)),
	
	/**
	 * Represents a war tortoise familiar.
	 */
	//WAR_TORTOISE_POUCH(46, 12031, 67, 58.6, 6815, 0.7, 7, new Item(12158), new Item(12155), new Item(7939), new Item(12183, 1)),
	
	/**
	 * Represents a bunyip familiar.
	 */
	//BUNYIP_POUCH(47, 12029, 68, 119.2, 6813, 1.4, 7, new Item(12159), new Item(12155), new Item(383), new Item(12183, 110)),
	
	/**
	 * Represents a fruit bat familiar.
	 */
	//FRUIT_BAT_POUCH(48, 12033, 69, 121.2, 6817, 1.4, 8, new Item(12159), new Item(12155), new Item(1963), new Item(12183, 130)),
	
	/**
	 * Represents a ravenous locust familiar.
	 */
	//RAVENOUS_LOCUST_POUCH(49, 12820, 70, 132, 7372, 1.5, 4, new Item(12160), new Item(12155), new Item(1933), new Item(12183, 79)),
	
	/**
	 * Represents an arctic bear familiar.
	 */
	//ARCTIC_BEAR_POUCH(50, 12057, 71, 93.2, 6839, 1.1, 8, new Item(12158), new Item(12155), new Item(10117), new Item(12183, 14)),
	
	/**
	 * Represents an obsidian golem familiar.
	 */
	//OBSIDIAN_GOLEM_POUCH(51, 12792, 73, 642.4, 7345, 7.3, 8, new Item(12163), new Item(12155), new Item(12168), new Item(12183, 195)),
	
	/**
	 * Represents a granite lobster familiar.
	 */
	//GRANITE_LOBSTER_POUCH(52, 12069, 74, 325.6, 6849, 3.7, 8, new Item(12160), new Item(12155), new Item(6979), new Item(12183, 166)),
	
	/**
	 * Represents a praying mantis familiar.
	 */
	//PRAYING_MANTIS_POUCH(53, 12011, 75, 329.6, 6798, 3.6, 8, new Item(12160), new Item(12155), new Item(2460), new Item(12183, 168)),
	
	/**
	 * Represents a forge regent familiar.
	 */
	//FORGE_REGENT_BEAST(54, 12782, 76, 134, 7335, 1.5, 9, new Item(12159), new Item(12155), new Item(10020), new Item(12183, 141)),
	
	/**
	 * Represents a talon beast familiar.
	 */
	//TALON_BEAST_POUCH(55, 12794, 77, 1015.2, 7347, 3.8, 9, new Item(12160), new Item(12155), new Item(12162), new Item(12183, 174)),
	
	/**
	 * Represents a giant ent familiar.
	 */
	//GIANT_ENT_POUCH(56, 12013, 78, 136.8, 6800, 1.6, 8, new Item(12159), new Item(5933), new Item(12155), new Item(12183, 124)),
	
	/**
	 * Represents a hydra familiar.
	 */
	//HYDRA_POUCH(60, 12025, 80, 140.8, 6811, 1.6, 9, new Item(12159), new Item(571), new Item( 12183, 128)),
	
	/**
	 * Represents a spirit dagannoth familiar.
	 */
	//SPIRIT_DAGANNOTH_POUCH(61, 12017, 83, 364.8, 6804, 4.1, 9, new Item(12160), new Item(6155), new Item( 12183, 1)),
	
	/**
	 * Represents a unicorn stallion familiar.
	 */
	//UNICORN_STALLION_POUCH(70, 12039, 89, 154.4, 6822, 1.8, 9, new Item(12159), new Item(237),  new Item( 12183, 203)),
	
	/**
	 * Represents a wolpertinger familiar.
	 */
	//WOLPERTINGER_POUCH(72, 12089, 92, 404.8, 6869, 4.5, 10, new Item(12160), new Item(2859), new Item(3226), new Item( 12183, 203)),
	
	/**
	 * Represents a pack yak familiar.
	 */
	//PACK_YAK_POUCH(75, 12093, 96, 422.4, 6873, 4.8, 10, new Item(12160), new Item(10818), new Item( 12183, 211)),
	
	/**
	 * Represents a fire titan familiar.
	 */
	//FIRE_TITAN_POUCH(57, 12802, 79, 695.2, 7355, 7.9, 9, new Item(12163), new Item(1442), new Item(12155), new Item(12183, 198)),
	
	/**
	 * Represents a moss titan familiar.
	 */
	//MOSS_TITAN_POUCH(58, 12804, 79, 695.2, 7357, 7.9, 9, new Item(12163), new Item(1440), new Item(12155), new Item(12183, 198)),
	
	/**
	 * Represents an ice titan familiar.
	 */
	//ICE_TITAN_POUCH(59, 12806, 79, 695.2,7359, 7.9, 9, new Item(12163), new Item(1438), new Item(1444), new Item(12155), new Item(12183, 198)),
	
	/**
	 * Represents a lava titan familiar.
	 */
	//LAVA_TITAN_POUCH(62, 12788, 83, 730.4, 7341, 8.3, 9, new Item(12163), new Item(12168), new Item(12155), new Item(12183, 219)),
	
	/**
	 * Represents a swamp titan familiar.
	 */
	//SWAMP_TITAN_POUCH(63, 12776, 85, 373.6, 7329, 4.2, 9, new Item(12160), new Item(10149), new Item(12155), new Item(12183, 150)),
	
	/**
	 * Represents a geyser titan familiar.
	 */
	//GEYSER_TITAN_POUCH(71, 12786, 89, 783.2, 7339, 8.9, 9, new Item(12163), new Item(1444), new Item(12155), new Item(12183, 222)),
	
	/**
	 * Represents an abyssal titan familiar.
	 */
	//ABYSSAL_TITAN_POUCH(73, 12796, 93, 163.2, 7349, 1.9, 10, new Item(12159), new Item(12161), new Item(12155), new Item(12183, 113)),
	
	/**
	 * Represents an iron titan familiar.
	 */
	//IRON_TITAN_POUCH(74, 12822, 95, 417.6, 7375, 4.7, 10, new Item(12160), new Item(1115), new Item(12155), new Item(12183, 198)),
	
	/**
	 * Represents a steel titan familiar.
	 */
	//STEEL_TITAN_POUCH(76, 12790, 99, 435.2, 7343, 4.9, 10, new Item(12160), new Item(1119), new Item(12155), new Item(12183, 178));
	
	/**
	 *  The slot id.
	 */
	private final int slot;
	
	/**
	 * The pouch item id.
	 */
	private final int pouchId;
	
	/**
	 * The level required to create this familiar.
	 */
	private final int levelRequired;
	
	/**
	 * The experience gained when creating this familiar.
	 */
	private final double createExperience;
	
	/**
	 * The familiar npc id.
	 */
	private final int npcId;
	
	/**
	 * The experience gained when summoning the familiar.
	 */
	private final double summonExperience;
	
	/**
	 * The summon cost.
	 */
	private final int summonCost;
	
	/**
	 * The lifespan of this familiar in minutes.
	 */
	private final int life;
	
	/**
	 * The used to create this familiar's pouch.
	 */
	private final Charm charm;
	
	/**
	 * The items required to create this pouch.
	 */
	private final Item[] items;
	
	/**
	 * Constructs a new {@code SummoningData} {@code Object}.
	 * @param pouchId The pouch item id.
	 * @param levelRequired The level required to create.
	 * @param createExperience The experience gained when creating a pouch.
	 * @param npcId The familiar's NPC id.
	 * @param summonExperience The experience gained when summoning.
	 * @param summonCost The amount of summoning points to drain when summoned.
	 * @param items The items required to create this familiar.
	 */
	SummoningData(int slot, int pouchId, int levelRequired, double createExperience, int npcId, double summonExperience, int summonCost, Charm charm, int life, Item...items) {
		this.slot = slot;
		this.pouchId = pouchId;
		this.levelRequired = levelRequired;
		this.createExperience = createExperience;
		this.npcId = npcId;
		this.summonExperience = summonExperience;
		this.summonCost = summonCost;
		this.charm = charm;
		this.life = life;
		this.items = items;
	}
	
	/**
	 * @return the pouchId
	 */
	public int getPouchId() {
		return pouchId;
	}
	
	/**
	 * @return the levelRequired
	 */
	public int getLevelRequired() {
		return levelRequired;
	}
	
	/**
	 * @return the createExperience
	 */
	public double getCreateExperience() {
		return createExperience;
	}
	
	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}
	
	/**
	 * @return the summonExperience
	 */
	public double getSummonExperience() {
		return summonExperience;
	}
	
	/**
	 * Gets the amount of points to decrease the summoning points with when summoned.
	 * @return The amount of points.
	 */
	public int getSummonCost() {
		return summonCost;
	}
	
	/**
	 * @return charm for pouch creation
	 */
	public Charm getCharm() {
		return charm;
	}
	
	/**
	 * @return the items
	 */
	public Item[] getItems() {
		return items;
	}
	
	/**
	 * @return life
	 */
	public int getLife() {
		return life;
	}
	
	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}
}
