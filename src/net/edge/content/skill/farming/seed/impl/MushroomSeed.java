package net.edge.content.skill.farming.seed.impl;

import net.edge.content.skill.farming.FarmingConstants;
import net.edge.content.skill.farming.patch.Patch;
import net.edge.content.skill.farming.seed.SeedClass;
import net.edge.content.skill.farming.seed.SeedType;
import net.edge.world.Animation;
import net.edge.world.entity.item.Item;

public enum MushroomSeed implements SeedType {
	
	BITTERCAP(53, new Item(5282, 1), //seeds
			new int[]{6, 40}, //growth time
			new Item[]{new Item(6004, 4)}, //rewards
			null, //protection fee
			new int[]{62, 0, 58}, //experience (plant, check_health, harvest)
			new int[]{0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A}),
	//4, 5, 6, 7, 8, 9, 10
	
	;
	
	MushroomSeed(int levelRequirement, Item seed, int[] growthTime, Item[] rewards, Item[] protectionFee, int[] experience, int[] values) {
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
		return SeedClass.MUSHROOMS;
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
		return 6;
	}
}
