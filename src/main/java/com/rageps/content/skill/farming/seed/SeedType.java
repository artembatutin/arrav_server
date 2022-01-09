package com.rageps.content.skill.farming.seed;

import com.rageps.content.skill.farming.patch.Patch;
import com.rageps.world.entity.item.Item;
import com.rageps.world.model.Animation;

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
