package net.arrav.content.minigame.clanwars;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SetupConstants {
	LAST_TEAM_STANDING(31081, 936, 31086, RuleCategory.GAME_END),
	KILLS_25(31082, 937, 31087, RuleCategory.GAME_END),
	KILLS_50(31083, 938, 31088, RuleCategory.GAME_END),
	KILLS_75(31084, 939, 31089, RuleCategory.GAME_END),
	KILLS_100(31085, 940, 31090, RuleCategory.GAME_END),
	
	ARENA_1(31091, 941, 31097, RuleCategory.ARENA),
	ARENA_2(31092, 942, 31098, RuleCategory.ARENA),
	ARENA_3(31093, 943, 31099, RuleCategory.ARENA),
	ARENA_4(31094, 944, 31100, RuleCategory.ARENA),
	ARENA_5(31095, 945, 31101, RuleCategory.ARENA),
	ARENA_6(31096, 945, 31102, RuleCategory.ARENA),
	
	IGNORE_FREEZING(31073, 930, 31077, RuleCategory.MISCELLANEOUS),
	PJ_TIMER(31074, 931, 31078, RuleCategory.MISCELLANEOUS),
	SINGLE_SPELLS(31075, 932, 31079, RuleCategory.MISCELLANEOUS),
	KEEP_ITEMS(31076, 933, 31080, RuleCategory.MISCELLANEOUS),
	
	MELEE_ALLOWED(31026, 899, 31047, RuleCategory.MELEE),
	MELEE_DISABLED(31027, 900, 31048, RuleCategory.MELEE),
	
	RANGING_ALLOWED(31035, 908, 31056, RuleCategory.RANGED),
	RANGING_DISABLED(31036, 909, 31057, RuleCategory.RANGED),
	
	ALL_SPELLBOOKS(31039, 912, 31060, RuleCategory.MAGIC),
	STANDARD_SPELLS(31040, 913, 31061, RuleCategory.MAGIC),
	BINDING_ONLY(31041, 914, 31062, RuleCategory.MAGIC),
	DISABLED(31042, 915, 31063, RuleCategory.MAGIC),
	
	FOOD_ALLOWED(31028, 901, 31049, RuleCategory.FOOD),
	FOOD_DISABLED(31029, 902, 31050, RuleCategory.FOOD),
	
	DRINKS_ALLOWED(31037, 910, 31058, RuleCategory.DRINKS),
	DRINKS_DISABLED(31038, 911, 31059, RuleCategory.DRINKS),
	
	SPECIALS_ALLOWED(31030, 903, 31051, RuleCategory.SPECIALS),
	NO_STAFF_OF_DEAD(31031, 904, 31052, RuleCategory.SPECIALS),
	SPECIALS_DISABLED(31032, 905, 31053, RuleCategory.SPECIALS),
	
	KILL_EM_ALL(31033, 906, 31054, RuleCategory.STRAGGLERS),
	IGNORE_5(31034, 907, 31055, RuleCategory.STRAGGLERS),
	
	PRAYER_ALLOWED(31043, 916, 31064, RuleCategory.PRAYER),
	NO_OVERHEADS(31044, 917, 31065, RuleCategory.PRAYER),
	PRAYER_DISABLED(31045, 918, 31066, RuleCategory.PRAYER);
	
	private final int button;
	private final int config;
	private final RuleCategory category;
	private final int string;

	public static final ImmutableMap<Integer, SetupConstants> VALUES = ImmutableMap.copyOf(Stream.of(SetupConstants.values()).collect(Collectors.toMap(p -> p.getButton(), Function.identity())));

	
	SetupConstants(int button, int config, int string, RuleCategory category) {
		this.button = button;
		this.config = config;
		this.category = category;
		this.string = string;
	}

	public int getButton() {
		return button;
	}

	public int getConfig() {
		return config;
	}

	public RuleCategory getCategory() {
		return category;
	}

	public int getString() {
		return string;
	}
}
