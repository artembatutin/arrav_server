package net.arrav.content.skill.farming.seed;

import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.world.Animation;
import net.arrav.world.entity.item.Item;

public interface SeedType {
	
	SeedClass getSeedClass();
	
	int getLevelRequirement();
	
	int getToolId();
	
	Animation getAnimation();
	
	Item getSeed();
	
	int[] getGrowthTime();
	
	Item[] getRewards();
	
	Item[] getProtectionFee();
	
	int[] getExperience();
	
	int[] getValues();
	
	int getHarvestAmount(Patch patch);
}
