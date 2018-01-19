package net.arrav.content.skill.farming.seed.impl;

import net.arrav.content.skill.farming.FarmingConstants;
import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.content.skill.farming.seed.SeedClass;
import net.arrav.content.skill.farming.seed.SeedType;
import net.arrav.world.Animation;
import net.arrav.world.entity.item.Item;

public enum TreeSeed implements SeedType {

	ACORN(15, new Item(5358), //seeds
			new int[]{5, 20}, //growth time
			new Item[]{new Item(1521)}, //rewards
			null, //protection fee
			new int[]{14, 467, 0}, //experience (plant, check_health, harvest)
			new int[]{0x08, 0x09, 0x0A, 0x0B, 0x0C}),
	//8, 9, 10, 11, 12, 13

	WILLOW(30, new Item(5371), //seeds
			new int[]{5, 28}, //growth time
			new Item[]{new Item(1519)}, //rewards
			null, //protection fee
			new int[]{25, 1457, 0}, //experience (plant, check_health, harvest)
			new int[]{0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15}),
	//15, 16, 17, 18, 19, 20, 21

	MAPLE(45, new Item(5372), //seeds
			new int[]{5, 32}, //growth time
			new Item[]{new Item(1517)}, //rewards
			null, //protection fee
			new int[]{45, 3403, 0}, //experience (plant, check_health, harvest)
			new int[]{0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20}),
	//24, 25, 26, 27, 28, 29, 30, 31, 32

	YEW(60, new Item(5373), //seeds
			new int[]{5, 38}, //growth time
			new Item[]{new Item(1515)}, //rewards
			null, //protection fee
			new int[]{81, 7070, 0}, //experience (plant, check_health, harvest)
			new int[]{0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D}),
	//35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45

	MAGIC(75, new Item(5374), //seeds
			new int[]{5, 46}, //growth time
			new Item[]{new Item(1513)}, //rewards
			null, //protection fee
			new int[]{146, 13768, 0}, //experience (plant, check_health, harvest)
			new int[]{0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C}),
	//48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60
	;

	TreeSeed(int levelRequirement, Item seed, int[] growthTime, Item[] rewards, Item[] protectionFee, int[] experience, int[] values) {
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
		return SeedClass.TREES;
	}

	@Override
	public int getLevelRequirement() {
		return levelRequirement;
	}

	@Override
	public int getToolId() {
		return FarmingConstants.SPADE_ITEM_ID;
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
		return 250;
	}

}
