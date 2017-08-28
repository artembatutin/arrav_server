package net.edge.content.skill.farming.seed;

import net.edge.content.skill.farming.patch.Patch;
import net.edge.world.Animation;
import net.edge.world.entity.item.Item;

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
