package net.arrav.content.skill.farming.seed.impl;

import net.arrav.content.skill.farming.FarmingConstants;
import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.content.skill.farming.seed.SeedClass;
import net.arrav.content.skill.farming.seed.SeedType;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Animation;
import net.arrav.world.entity.item.Item;

public enum AllotmentSeed implements SeedType {
	
	POTATO(1, new Item(5318, 3), //seeds
			new int[]{4, 5}, //growth time
			new Item[]{new Item(1942)}, //rewards
			new Item[]{new Item(FarmingConstants.COMPOST_ITEM_ID, 2)}, //protection fee
			new int[]{8, 0, 9}, //experience (plant, check_health, harvest)
			new int[]{0x06, 0x07, 0x08, 0x09, 0x0C}),
	//6, 7, 8, 9, 12
	
	ONION(5, new Item(5319, 3), //seeds
			new int[]{4, 5}, //growth time
			new Item[]{new Item(1957)}, //rewards
			new Item[]{new Item(5438)}, //protection fee
			new int[]{10, 0, 11}, //experience (plant, check_health, harvest)
			new int[]{0x0D, 0x0E, 0x0F, 0x10, 0x13}),
	//13, 14, 15, 16, 19
	
	CABBAGE(7, new Item(5324, 3), //seeds
			new int[]{4, 5}, //growth time
			new Item[]{new Item(1965)}, //rewards
			new Item[]{new Item(5458)}, //protection fee
			new int[]{10, 0, 12}, //experience (plant, check_health, harvest)
			new int[]{0x14, 0x15, 0x16, 0x17, 0x1A}),
	//20, 21, 22, 23, 26
	
	TOMATO(12, new Item(5322, 3), //seeds
			new int[]{4, 5}, //growth time
			new Item[]{new Item(1982)}, //rewards
			new Item[]{new Item(5478, 2)}, //protection fee
			new int[]{13, 0, 14}, //experience (plant, check_health, harvest)
			new int[]{0x1B, 0x1C, 0x1D, 0x1E, 0x21}),
	//27, 28, 29, 30, 33
	
	SWEETCORN(20, new Item(5320, 3), //seeds
			new int[]{5, 5}, //growth time
			new Item[]{new Item(5986)}, //rewards
			new Item[]{new Item(5931, 10)}, //protection fee
			new int[]{17, 0, 19}, //experience (plant, check_health, harvest)
			new int[]{0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x2A}),
	//34, 35, 36, 37, 38, 39, 42
	
	STRAWBERRY(31, new Item(5323, 3), //seeds
			new int[]{6, 5}, //growth time
			new Item[]{new Item(5504)}, //rewards
			new Item[]{new Item(5386)}, //protection fee
			new int[]{26, 0, 29}, //experience (plant, check_health, harvest)
			new int[]{0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x33}),
	//43, 44, 45, 46, 47, 48, 51
	
	WATERMELON(47, new Item(5321, 3), //seeds
			new int[]{8, 6}, //growth time
			new Item[]{new Item(5982)}, //rewards
			new Item[]{new Item(5970, 10)}, //protection fee
			new int[]{49, 0, 55}, //experience (plant, check_health, harvest)
			new int[]{0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3E}),
	//52, 53, 54, 55, 56, 57, 58, 59, 62
	;
	
	AllotmentSeed(int levelRequirement, Item seed, int[] growthTime, Item[] rewards, Item[] protectionFee, int[] experience, int[] values) {
		this.levelRequirement = levelRequirement;
		this.seed = seed;
		this.growthTime = growthTime;
		this.rewards = rewards;
		this.protectionFee = protectionFee;
		this.experience = experience;
		this.values = values;
	}
	
	private final int levelRequirement;
	
	private final Item seed;
	
	private final int[] growthTime;
	
	private final Item[] rewards;
	
	private final Item[] protectionFee;
	
	private final int[] experience;
	
	private final int[] values;
	
	@Override
	public SeedClass getSeedClass() {
		return SeedClass.ALLOTMENT;
	}
	
	@Override
	public int getLevelRequirement() {
		return levelRequirement;
	}
	
	@Override
	public int getToolId() {
		return -1;
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
		return 3 + RandomUtils.inclusive(53);
	}
}
