package net.edge.content.skill.farming.seed.impl;

import net.edge.content.skill.farming.FarmingConstants;
import net.edge.content.skill.farming.attributes.PatchAttribute;
import net.edge.content.skill.farming.patch.Patch;
import net.edge.content.skill.farming.seed.SeedClass;
import net.edge.content.skill.farming.seed.SeedType;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.item.Item;

public enum HerbSeed implements SeedType {

	GUAM(9, new Item(5291), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(199)}, //rewards
			null, //protection fee
			new int[]{11, 0, 13}, //experience (plant, check_health, harvest)
			new int[]{0x04, 0x05, 0x06, 0x07, 0x08}),
	//4, 5, 6, 7, 8

	MARRENTIL(14, new Item(5292), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(201)}, //rewards
			null, //protection fee
			new int[]{14, 0, 15}, //experience (plant, check_health, harvest)
			new int[]{0x0B, 0x0C, 0x0D, 0x0E, 0x0F}),
	//11, 12, 13, 14, 15

	TARROMIN(19, new Item(5293), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(203)}, //rewards
			null, //protection fee
			new int[]{16, 0, 18}, //experience (plant, check_health, harvest)
			new int[]{0x12, 0x13, 0x14, 0x15, 0x16}),
	//18, 19, 20, 21, 22

	HARRALANDER(26, new Item(5294), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(205)}, //rewards
			null, //protection fee
			new int[]{22, 0, 24}, //experience (plant, check_health, harvest)
			new int[]{0x19, 0x1A, 0x1B, 0x1C, 0x1D}),
	//25, 26, 27, 28, 29

	//GOUT_TUBER(29),

	RANARR(32, new Item(5295), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(207)}, //rewards
			null, //protection fee
			new int[]{27, 0, 31}, //experience (plant, check_health, harvest)
			new int[]{0x20, 0x21, 0x22, 0x23, 0x24}),
	//32, 33, 34, 35, 36

	SPIRIT_WEED(36, new Item(12176), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(12174)}, //rewards
			null, //protection fee
			new int[]{27, 0, 31}, //experience (plant, check_health, harvest)
			new int[]{0x27, 0x28, 0x29, 0x2A, 0x2B}),
	//39, 40, 41, 42, 43

	TOADFLAX(38, new Item(5296), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(3049)}, //rewards
			null, //protection fee
			new int[]{34, 0, 39}, //experience (plant, check_health, harvest)
			new int[]{0x2E, 0x2F, 0x30, 0x31, 0x32}),
	//46, 47, 48, 49, 50

	IRIT(44, new Item(5297), //seeds
			new int[]{1, 20}, //growth time
			new Item[]{new Item(209)}, //rewards
			null, //protection fee
			new int[]{43, 0, 49}, //experience (plant, check_health, harvest)
			new int[]{0x35, 0x36, 0x37, 0x38, 0x39}),
	//53, 54, 55, 56, 57

	WERGALI(46, new Item(14870), //seeds
			new int[]{1, 20}, //growth time
			new Item[]{new Item(14836)}, //rewards
			null, //protection fee
			new int[]{43, 0, 49}, //experience (plant, check_health, harvest)
			new int[]{0x3C, 0x3D, 0x3E, 0x3F, 0x40}),
	//60, 61, 62, 63, 64

	AVANTOE(50, new Item(5298), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(211)}, //rewards
			null, //protection fee
			new int[]{55, 0, 62}, //experience (plant, check_health, harvest)
			new int[]{0x44, 0x45, 0x46, 0x47, 0x48}),
	//67, 68, 69, 70, 71, 72

	KWUARM(56, new Item(5299), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(213)}, //rewards
			null, //protection fee
			new int[]{69, 0, 78}, //experience (plant, check_health, harvest)
			new int[]{0x4B, 0x4C, 0x4D, 0x4E, 0x4F}),
	//75, 76, 77, 78, 79

	SNAPDRAGON(62, new Item(5300), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(3051)}, //rewards
			null, //protection fee
			new int[]{88, 0, 99}, //experience (plant, check_health, harvest)
			new int[]{0x53, 0x54, 0x55, 0x56, 0x57}),
	//82, 83, 84, 85, 86

	CADANTINE(67, new Item(5301), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(215)}, //rewards
			null, //protection fee
			new int[]{107, 0, 120}, //experience (plant, check_health, harvest)
			new int[]{0x59, 0x5A, 0x5B, 0x5C, 0x5D}),
	//89, 90, 91, 92, 93

	LANTADYME(73, new Item(5302), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(2485)}, //rewards
			null, //protection fee
			new int[]{135, 0, 152}, //experience (plant, check_health, harvest)
			new int[]{0x60, 0x61, 0x62, 0x63, 0x64}),
	//96, 97, 98, 99, 100

	DWARF_WEED(79, new Item(5303), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(217)}, //rewards
			null, //protection fee
			new int[]{171, 0, 192}, //experience (plant, check_health, harvest)
			new int[]{0x67, 0x68, 0x69, 0x6A, 0x6B}),
	//103, 104, 105, 106, 107

	TORSTOL(85, new Item(5304), //seeds
			new int[]{4, 10}, //growth time
			new Item[]{new Item(219)}, //rewards
			null, //protection fee
			new int[]{200, 0, 225}, //experience (plant, check_health, harvest)
			new int[]{0x67, 0x68, 0x69, 0x6A, 0x6B}),
	//110, 111, 112, 113, 114
	;

	HerbSeed(int levelRequirement, Item seed, int[] growthTime, Item[] rewards, Item[] protectionFee, int[] experience, int[] values) {
		this.levelRequirement = levelRequirement;
		this.seed = seed;
		this.growthTime = growthTime;
		this.rewards = rewards;
		this.protectionFee = protectionFee;
		this.experience = experience;
		this.values = values;
	}

	private final int levelRequirement;

	private final int[] growthTime;

	private final Item seed;

	private final Item[] rewards;

	private final Item[] protectionFee;

	private final int[] experience;

	private final int[] values;

	@Override
	public SeedClass getSeedClass() {
		return SeedClass.HERBS;
	}

	@Override
	public int getLevelRequirement() {
		return levelRequirement;
	}

	@Override
	public int getToolId() {
		return FarmingConstants.SEED_DIBBER_ITEM_ID;
	}

	@Override
	public Animation getAnimation() {
		return FarmingConstants.GENERAL_SEEDING_ANIMATION;
	}

	@Override
	public int[] getGrowthTime() {
		return growthTime;
	}

	@Override
	public Item getSeed() {
		return seed;
	}

	@Override
	public Item[] getRewards() {
		return rewards;
	}

	@Override
	public Item[] getProtectionFee() {
		return protectionFee;
	}

	@Override
	public int[] getExperience() {
		return experience;
	}

	@Override
	public int[] getValues() {
		return values;
	}

	@Override
	public int getHarvestAmount(Patch patch) {
		if(patch.hasAttribute(PatchAttribute.COMPOSTED)) {
			return 5 + RandomUtils.inclusive(22);
		}
		return 3 + RandomUtils.inclusive(15);
	}
}
