package net.arrav.content.minigame.clanwars;

import com.google.common.collect.ImmutableSet;

public enum RuleCategory {
	GAME_END, ARENA, MISCELLANEOUS, MELEE, RANGED, MAGIC, FOOD, DRINKS, SPECIALS, PRAYER, STRAGGLERS;
	
	RuleCategory() {
	}

	public static ImmutableSet<RuleCategory> VALUES;

}