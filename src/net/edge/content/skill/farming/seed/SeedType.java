package net.edge.content.skill.farming.seed;

import net.edge.content.skill.farming.patch.Patch;
import net.edge.world.Animation;
import net.edge.world.entity.item.Item;

public interface SeedType {
	
	public SeedClass getSeedClass();
	
	public int getLevelRequirement();
	
	public int getToolId();
	
	public Animation getAnimation();
	
	public Item getSeed();
	
	public int[] getGrowthTime();
	
	public Item[] getRewards();
	
	public Item[] getProtectionFee();
	
	public int[] getExperience();
	
	public int[] getValues();
	
	public int getHarvestAmount(Patch patch);
}
