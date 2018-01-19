package net.arrav.content.skill.farming.seed.impl;

import net.arrav.content.skill.farming.FarmingConstants;
import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.content.skill.farming.seed.SeedClass;
import net.arrav.content.skill.farming.seed.SeedType;
import net.arrav.world.Animation;
import net.arrav.world.entity.item.Item;

public enum BushSeed implements SeedType {

	REDBERRY(10, new Item(5101, 1), //seeds
			new int[]{5, 10}, //growth time
			new Item[]{new Item(1951, 4)}, //rewards
			new Item[]{new Item(5466), new Item(5468), new Item(5470), new Item(5472), new Item(5474), new Item(5476), new Item(5478)}, //protection fee
			new int[]{12, 64, 5}, //experience (plant, check_health, harvest)
			new int[]{0x05, 0x06, 0x07, 0x08, 0x0A, 0x0E}),
	//5, 6, 7, 8, 9, 14

	CADAVABERRY(22, new Item(5102, 1), //seeds
			new int[]{6, 10}, //growth time
			new Item[]{new Item(753, 4)}, //rewards
			new Item[]{new Item(5964), new Item(5966), new Item(5968)}, //protection fee
			new int[]{18, 103, 7}, //experience (plant, check_health, harvest)
			new int[]{0x0F, 0x10, 0x11, 0x12, 0x13, 0x19}),
	//15, 16, 17, 18, 19, 25

	DWELLVERRY(36, new Item(5103, 1), //seeds
			new int[]{7, 10}, //growth time
			new Item[]{new Item(2126, 4)}, //rewards
			new Item[]{new Item(5402), new Item(5404), new Item(5406)}, //protection fee
			new int[]{32, 178, 12}, //experience (plant, check_health, harvest)
			new int[]{0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x25}),
	//26, 27, 28, 29, 30, 31, 37

	JANGERBERRY(48, new Item(5104, 1), //seeds
			new int[]{8, 10}, //growth time
			new Item[]{new Item(247, 4)}, //rewards
			new Item[]{new Item(5982, 6)}, //protection fee
			new int[]{51, 285, 19}, //experience (plant, check_health, harvest)
			new int[]{0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x32}),
	//38, 39, 40, 41, 42, 43, 50

	WHITEBERRY(59, new Item(5105, 1), //seeds
			new int[]{8, 10}, //growth time
			new Item[]{new Item(239, 4)}, //rewards
			new Item[]{new Item(6004, 8)}, //protection fee
			new int[]{78, 438, 29}, //experience (plant, check_health, harvest)
			new int[]{0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3F}),
	//51, 52, 53, 54, 55, 56, 57, 63

	POISON_IVY(70, new Item(5106, 1), //seeds
			new int[]{8, 10}, //growth time
			new Item[]{new Item(6018, 4)}, //rewards
			null, //protection fee
			new int[]{120, 675, 45}, //experience (plant, check_health, harvest)
			new int[]{0xC5, 0xC6, 0xC7, 0xC8, 0xC9, 0xCA, 0xCB, 0xD1}),
	//197, 198, 199, 200, 201, 202, 203, 209
	;

	BushSeed(int levelRequirement, Item seeds, int[] growthTime, Item[] rewards, Item[] protectionFee, int[] experience, int[] values) {
		this.levelRequirement = levelRequirement;
		this.seeds = seeds;
		this.growthTime = growthTime;
		this.rewards = rewards;
		this.protectionFee = protectionFee;
		this.experience = experience;
		this.values = values;
	}

	private final int levelRequirement;

	private final int[] growthTime;

	private final Item seeds;

	private final Item[] rewards;

	private final Item[] protectionFee;

	private final int[] experience;

	private final int[] values;

	@Override
	public SeedClass getSeedClass() {
		return SeedClass.BUSHES;
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
		return seeds;
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
		return 4;
	}
}
