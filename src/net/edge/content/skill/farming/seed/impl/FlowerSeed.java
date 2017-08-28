package net.edge.content.skill.farming.seed.impl;

import net.edge.content.skill.farming.FarmingConstants;
import net.edge.content.skill.farming.patch.Patch;
import net.edge.content.skill.farming.seed.SeedClass;
import net.edge.content.skill.farming.seed.SeedType;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.item.Item;

public enum FlowerSeed implements SeedType {

	/**
	 * [Flower]:
	 * [Marigold]: 508 8 to 12 - 5096
	 * [Rosemary]: 508 13 to 17 - 5097
	 * [Nasturtium]: 508 18 to 22 - 5098
	 * [Woad plant]: 508 23 to 27 - 5099
	 * [Limpwurt plant]: 508 28 to 32 - 5100
	 * [Scarecrow]: 508 33 to 36 [37 = cleaned patch]
	 * [Lily]: UNSUPPORTER 508 38 to 41
	 */
	MARIGOLD(2, new Item(5096), //seeds
			new int[]{2, 5}, //growth time
			new Item[]{new Item(6010)}, //rewards
			null, //protection fee
			new int[]{9, 0, 47}, //experience (plant, check_health, harvest)
			new int[]{0x08, 0x09, 0x0A, 0x0B, 0x0C}),
	//8, 9, 10, 11, 12

	ROSEMARY(11, new Item(5097), //seeds
			new int[]{2, 5}, //growth time
			new Item[]{new Item(6014)}, //rewards
			null, //protection fee
			new int[]{12, 0, 67}, //experience (plant, check_health, harvest)
			new int[]{0x0D, 0x0E, 0x0F, 0x10, 0x11}),
	//13, 14, 15, 16, 17

	NASTURTIUM(24, new Item(5098), //seeds
			new int[]{2, 5}, //growth time
			new Item[]{new Item(6012)}, //rewards
			null, //protection fee
			new int[]{20, 0, 111}, //experience (plant, check_health, harvest)
			new int[]{0x12, 0x13, 0x14, 0x15, 0x16}),
	//18, 19, 20, 21, 22

	WOAD(25, new Item(5099), //seeds
			new int[]{2, 5}, //growth time
			new Item[]{new Item(1793)}, //rewards
			null, //protection fee
			new int[]{21, 0, 116}, //experience (plant, check_health, harvest)
			new int[]{0x17, 0x18, 0x19, 0x1A, 0x1B}),
	//23, 24, 25, 26, 27

	LIMPWURT(25, new Item(5100), //seeds
			new int[]{2, 5}, //growth time
			new Item[]{new Item(225)}, //rewards
			null, //protection fee
			new int[]{22, 19, 120}, //experience (plant, check_health, harvest)
			new int[]{0x1C, 0x1D, 0x1E, 0x1F, 0x20}),
	//28, 29, 30, 31, 32

	WHITE_LILY(52, new Item(14589), //seeds
			new int[]{3, 20}, //growth time
			new Item[]{new Item(14583)}, //rewards
			null, //protection fee
			new int[]{55, 19, 230}, //experience (plant, check_health, harvest)
			new int[]{0x26, 0x27, 0x28, 0x29}),
	//38, 39, 40, 41
	;

	FlowerSeed(int levelRequirement, Item seed, int[] growthTime, Item[] rewards, Item[] protectionFee, int[] experience, int[] values) {
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
		return SeedClass.FLOWERS;
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
		return 3 + RandomUtils.inclusive(2);
	}
}
