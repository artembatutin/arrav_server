package com.rageps.content.skill.farming.patch;

import com.google.common.collect.ImmutableSet;
import com.rageps.content.skill.farming.seed.impl.*;
import com.rageps.content.skill.farming.seed.SeedType;
import com.rageps.content.skill.farming.seed.impl.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PatchType {
	
	VARROCK_CASTLE_TREE_PATCH(502, 8390, TreeSeed.values()),
	HOME_EAST_TREE_PATCH(502, 8389, TreeSeed.values()),
	//MOVED TO HOME EAST - FALADOR'S
	HOME_WEST_TREE_PATCH(502, 8388, TreeSeed.values()),
	//MOVED TO HOME WEST - TAVERLEY'S
	
	FALADOR_NORTH_WEST_ALLOTMENT(504, 8550, AllotmentSeed.values()),
	FALADOR_SOUTH_EAST_ALLOTMENT(504, 8551, AllotmentSeed.values()),
	CATHERBY_NORTH_ALLOTMENT(504, 8552, AllotmentSeed.values()),
	CATHERBY_SOUTH_ALLOTMENT(504, 8553, AllotmentSeed.values()),
	
	ARDOUGNE_NORTH_ALLOTMENT(505, 8554, AllotmentSeed.values()),
	ARDOUGNE_SOUTH_ALLOTMENT(505, 8555, AllotmentSeed.values()),
	CANIFIS_NORTH_WEST_ALLOTMENT(505, 8556, AllotmentSeed.values()),
	CANIFIS_SOUTH_EAST_ALLOTMENT(505, 8557, AllotmentSeed.values()),
	
	FALADOR_FLOWER_PATCH(508, 7847, FlowerSeed.values()),
	CATHERBY_FLOWER_PATCH(508, 7848, FlowerSeed.values()),
	ARDOUGNE_FLOWER_PATCH(508, 7849, FlowerSeed.values()),
	CANIFIS_FLOWER_PATCH(508, 7850, FlowerSeed.values()),
	
	VARROCK_BUSH_PATCH(509, 7577, BushSeed.values()),
	RIMMINGTON_BUSH_PATCH(509, 7578, BushSeed.values()),
	ARDOUGNE_BUSH_PATCH(509, 7580, BushSeed.values()),
	ETCETERIA_BUSH_PATCH(509, 7579, BushSeed.values()),
	
	CANAFIS_MUSHROOM_PATCH(512, 8337, MushroomSeed.values()),
	
	FALADOR_HERB_PATCH(515, 8150, HerbSeed.values()),
	CATHERBY_HERB_PATCH(515, 8151, HerbSeed.values()),
	ARDOUGNE_HERB_PATCH(515, 8152, HerbSeed.values()),
	CANIFIS_HERB_PATCH(515, 8153, HerbSeed.values()),
	;
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<PatchType> VALUES = ImmutableSet.copyOf(values());
	
	PatchType(int configId, int objectId, SeedType[]... validSeedsArray) {
		this.configId = configId;
		this.objectId = objectId;
		validSeeds = new ArrayList<>();
		for(SeedType[] seedTypes : validSeedsArray) {
			Collections.addAll(validSeeds, seedTypes);
		}
	}
	
	private final int configId;
	
	private final int objectId;
	
	private final List<SeedType> validSeeds;
	
	public int getConfigId() {
		return configId;
	}
	
	public int getObjectId() {
		return objectId;
	}
	
	public List<SeedType> getValidSeeds() {
		return validSeeds;
	}
	
}
