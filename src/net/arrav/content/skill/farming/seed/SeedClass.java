package net.arrav.content.skill.farming.seed;

import net.arrav.content.skill.farming.seed.impl.*;

public enum SeedClass {
	
	ALLOTMENT {
		@Override
		public SeedType getInstance(String name) {
			return AllotmentSeed.valueOf(name);
		}
	}, FLOWERS {
		@Override
		public SeedType getInstance(String name) {
			return FlowerSeed.valueOf(name);
		}
	}, BUSHES {
		@Override
		public SeedType getInstance(String name) {
			return BushSeed.valueOf(name);
		}
	}, TREES {
		@Override
		public SeedType getInstance(String name) {
			return TreeSeed.valueOf(name);
		}
	}, HERBS {
		@Override
		public SeedType getInstance(String name) {
			return HerbSeed.valueOf(name);
		}
	}, MUSHROOMS {
		@Override
		public SeedType getInstance(String name) {
			return MushroomSeed.valueOf(name);
		}
	}, FRUIT_TREES, HOPS, SPECIAL_PLANTS,;
	
	public SeedType getInstance(String name) {
		return null;
	}
}
