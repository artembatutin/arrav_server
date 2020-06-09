package com.rageps.content.clanchannel.content;

import com.rageps.content.clanchannel.ClanType;
import com.rageps.content.clanchannel.channel.ClanChannel;
import com.rageps.util.Difficulty;
import com.rageps.util.rand.InclusiveRandom;
import com.rageps.util.rand.RandomUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds all the clan task data.
 *
 * @author Daniel
 */
public enum ClanTask {
	/** PvP */
	/* -> Easy <- */
	KILL_PLAYERS_I(ClanType.PVP, Difficulty.EASY, "Kill % players", ClanTaskKey.PLAYER_KILLING, new InclusiveRandom(10, 20)),
	KILL_BOT_I(ClanType.PVP, Difficulty.EASY, "Kill % bots", ClanTaskKey.BOT_KILLING, new InclusiveRandom(10, 25)),

	/* -> Medium <- */
	KILL_PLAYERS_II(ClanType.PVP, Difficulty.MEDIUM, "Kill % players", ClanTaskKey.PLAYER_KILLING, new InclusiveRandom(25, 35)),
	KILL_BOT_II(ClanType.PVP, Difficulty.MEDIUM, "Kill % bots", ClanTaskKey.BOT_KILLING, new InclusiveRandom(25, 50)),

	/* -> Hard <- */
	KILL_PLAYERS_III(ClanType.PVP, Difficulty.HARD, "Kill % players", ClanTaskKey.PLAYER_KILLING, new InclusiveRandom(50, 125)),
	KILL_BOT_III(ClanType.PVP, Difficulty.HARD, "Kill % bots", ClanTaskKey.BOT_KILLING, new InclusiveRandom(50, 125)),

	/** PvM */
	/* -> Easy <- */
	KILL_HILL_GIANTS(ClanType.PVM, Difficulty.EASY, "Kill % hill giants", ClanTaskKey.HILL_GIANT, new InclusiveRandom(100, 200)),
	KILL_ROCK_CRABS(ClanType.PVM, Difficulty.EASY, "Kill % rock crabs", ClanTaskKey.ROCK_CRAB, new InclusiveRandom(100, 200)),
	KILL_SAND_CRABS(ClanType.PVM, Difficulty.EASY, "Kill % sand crabs", ClanTaskKey.SAND_CRAB, new InclusiveRandom(100, 200)),
	KILL_GREEN_DRAGONS_I(ClanType.PVM, Difficulty.MEDIUM, "Kill % green dragons", ClanTaskKey.GREEN_DRAGON,
			new InclusiveRandom(50, 150)),

	/* -> Easy <- */
	/* -> Easy <- */
	ANY_TREE(ClanType.IRON_MAN, Difficulty.EASY, "Chop % Logs (Any Tree!)", ClanTaskKey.CHOP_ANY_LOG,
			new InclusiveRandom(150, 350)),
	/*
	 * COMPLETE_MASTERMINER_TASK(ClanType.SOCIAL, Difficulty.EASY,
	 * "Complete %s MasterMiner Tasks!", COMPLETE_MASTERMINER_TASK_KEY, new
	 * InclusiveRandom(2, 6)),
	 */
	/* -> Hard <- */
	TRVIA_TASK(ClanType.SOCIAL, Difficulty.HARD, "Answer % Trivia Questions", ClanTaskKey.TRIVIA_ANSWER_KEY,
			new InclusiveRandom(45, 70)),

	/* -> Medium <- */
	KILL_BLACK_DEMONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % black demons", ClanTaskKey.BLACK_DEMON,
			new InclusiveRandom(150, 250)),
	KILL_GREATER_DEMONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % greater demons", ClanTaskKey.GREATER_DEMON,
			new InclusiveRandom(150, 250)),
	KILL_BLUE_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % blue dragons", ClanTaskKey.BLUE_DRAGON,
			new InclusiveRandom(151, 250)),
	KILL_BLACK_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % black dragons", ClanTaskKey.BLACK_DRAGON,
			new InclusiveRandom(151, 250)),
	KILL_RED_DRAGONS(ClanType.PVM, Difficulty.MEDIUM, "Kill % red dragons", ClanTaskKey.RED_DRAGON, new InclusiveRandom(151, 250)),
	KILL_GREEN_DRAGONS_II(ClanType.PVM, Difficulty.MEDIUM, "Kill % green dragons", ClanTaskKey.GREEN_DRAGON,
			new InclusiveRandom(151, 250)),
	AFK_TREE(ClanType.SOCIAL, Difficulty.MEDIUM, "Harvest % AFK Logs", ClanTaskKey.AFK_LOG, new InclusiveRandom(800, 1200)),

	/* -> Hard <- */
	KILL_ABYSSAL_DEMONS(ClanType.PVM, Difficulty.HARD, "Kill % abyssal demons", ClanTaskKey.ABYSSAL_DEMON,
			new InclusiveRandom(250, 500)),
	KILL_SKELETAL_WYVERNS(ClanType.PVM, Difficulty.HARD, "Kill % skeletal wyverns", ClanTaskKey.SKELETAL_WYVERN,
			new InclusiveRandom(250, 500)),
	KILL_KING_BLACK_DRAGONS(ClanType.PVM, Difficulty.HARD, "Kill % king black dragons", ClanTaskKey.KING_BLACK_DRAGON,
			new InclusiveRandom(50, 200)),
	KILL_SCORPIA(ClanType.PVM, Difficulty.HARD, "Kill % scorpias", ClanTaskKey.SCORPIA, new InclusiveRandom(25, 75)),
	KILL_VENENATIS(ClanType.PVM, Difficulty.HARD, "Kill % venenatis", ClanTaskKey.VENNANTIS, new InclusiveRandom(25, 75)),
	KILL_CALLISTO(ClanType.PVM, Difficulty.HARD, "Kill % callistos", ClanTaskKey.CALLISTO, new InclusiveRandom(25, 75)),
	KILL_CRAZY_ARCHAEOLOGIST(ClanType.PVM, Difficulty.HARD, "Kill % crazy archaeologists", ClanTaskKey.CRAZY_ARCHAEOLOGIST,
			new InclusiveRandom(50, 200)),
	KILL_CHAOS_FANATIC(ClanType.PVM, Difficulty.HARD, "Kill % chaos fanatics", ClanTaskKey.CHAOS_FANATIC,
			new InclusiveRandom(25, 75)),
	KILL_CHAOS_ELEMENTAL(ClanType.PVM, Difficulty.HARD, "Kill % chaos elementals", ClanTaskKey.CHAOS_ELEMENTAL,
			new InclusiveRandom(25, 75)),

	/** Skilling */
	/* -> Easy <- */
	THIEVE_FROM_STALL_I(ClanType.SKILLING, Difficulty.EASY, "Thieve from stall % times", ClanTaskKey.THIEVING_STALL,
			new InclusiveRandom(300, 500)),
	CHOP_WILLOW(ClanType.SKILLING, Difficulty.EASY, "Chop % willow logs", ClanTaskKey.CHOP_WILLOW_LOG,
			new InclusiveRandom(300, 500)),
	COMPLETE_AGILITY_LAP_I(ClanType.SKILLING, Difficulty.EASY, "Complete % non-rooftop course", ClanTaskKey.AGILITY_COURSE,
			new InclusiveRandom(300, 500)),
	BURN_YEW_LOG(ClanType.SKILLING, Difficulty.EASY, "Burn % willow logs", ClanTaskKey.BURN_WILLOW_LOG,
			new InclusiveRandom(300, 500)),

	/* -> Medium <- */
	CATCH_SHARK(ClanType.SKILLING, Difficulty.MEDIUM, "Catch % sharks", ClanTaskKey.SHARK, new InclusiveRandom(500, 750)),
	CHOP_YEW(ClanType.SKILLING, Difficulty.MEDIUM, "Chop % yew logs", ClanTaskKey.YEW_LOG, new InclusiveRandom(500, 750)),
	FLETCH_YEW_SHORTBOW(ClanType.SKILLING, Difficulty.MEDIUM, "Fletch % yew shortbow", ClanTaskKey.YEW_SHORTBOW,
			new InclusiveRandom(500, 750)),
	CRAFT_DEATH_RUNE(ClanType.SKILLING, Difficulty.MEDIUM, "Craft % death runes", ClanTaskKey.DEATH_RUNE,
			new InclusiveRandom(500, 750)),
	CREATE_SUPER_RESTORE_POTION(ClanType.SKILLING, Difficulty.MEDIUM, "Create % super restore potions",
			ClanTaskKey.SUPER_RESTORE_POTION, new InclusiveRandom(500, 750)),
	COMPLETE_AGILITY_LAP_II(ClanType.SKILLING, Difficulty.MEDIUM, "Complete % non-rooftop course", ClanTaskKey.AGILITY_COURSE,
			new InclusiveRandom(500, 750)),
	THIEVE_FROM_STALL_II(ClanType.SKILLING, Difficulty.MEDIUM, "Thieve from stall % times", ClanTaskKey.THIEVING_STALL,
			new InclusiveRandom(500, 750)),

	/* -> Hard <- */
	// SMITH_RUNITE_BAR(ClanType.SKILLING, Difficulty.HARD, "Smith % runite bars",
	// RUNITE_BAR, new InclusiveRandom(600, 900)),
	FLETCH_MAGIC_SHORTBOW(ClanType.SKILLING, Difficulty.HARD, "Fletch % magic shortbow", ClanTaskKey.MAGIC_SHORTBOW,
			new InclusiveRandom(1250, 2000)),
	MINE_RUNITE_ORE(ClanType.SKILLING, Difficulty.HARD, "Mine % runite ores", ClanTaskKey.RUNITE_ORES,
			new InclusiveRandom(650, 900)),
//    CREATE_SUPER_COMBAT_POTION(ClanType.SKILLING, Difficulty.HARD, "Create % super combat potions", SUPER_COMBAT_POTION, new InclusiveRandom(, 2000)),
	COMPLETE_AGILITY_LAP_III(ClanType.SKILLING, Difficulty.HARD, "Complete % non-rooftop course", ClanTaskKey.AGILITY_COURSE,
			new InclusiveRandom(750, 1000)),
	CATCH_ANGLERFISH(ClanType.SKILLING, Difficulty.HARD, "Catch % dark crabs", ClanTaskKey.DARK_CRAB,
			new InclusiveRandom(750, 1000)),
	CRAFT_BLOOD_RUNE(ClanType.SKILLING, Difficulty.HARD, "Craft % blood runes", ClanTaskKey.BLOOD_RUNE,
			new InclusiveRandom(750, 1000)),
	CHOP_MAGIC(ClanType.SKILLING, Difficulty.HARD, "Chop % magic logs", ClanTaskKey.MAGIC_LOG, new InclusiveRandom(600, 800));

	public final String task;
	public final ClanTaskKey key;
	public final InclusiveRandom amount;
	public final Difficulty difficulty;
	public final ClanType type;

	ClanTask(ClanType type, Difficulty difficulty, String task, ClanTaskKey key, InclusiveRandom amount) {
		this.task = task;
		this.key = key;
		this.amount = amount;
		this.difficulty = difficulty;
		this.type = type;
	}

	public double getProgressExperience() {
		switch (type) {
		case PVP:
			return 1250;
		case PVM:
			return difficulty == Difficulty.HARD ? 350 : 50;
		case SKILLING:
			return 5;
		}
		return 0;
	}

	public String getName(ClanChannel channel) {
		return task.replace("%", "" + channel.getDetails().taskAmount);
	}

	public int getAmount() {
		return RandomUtils.random(amount.minimum, amount.maximum);
	}

	public static Set<ClanTask> getTasks(ClanType type, Difficulty difficulty) {
		Set<ClanTask> tasks = new HashSet<>();
		for (ClanTask task : values()) {
			if (task.type == type && task.difficulty == difficulty)
				tasks.add(task);
		}
		return tasks;
	}

	public static ClanTask getAssignment(ClanType type, Difficulty difficulty) {
		Set<ClanTask> tasks = getTasks(type, difficulty);
		return RandomUtils.random(tasks);
	}

	public int getReward() {
		return (difficulty == Difficulty.EASY) ? 2 : (difficulty == Difficulty.MEDIUM) ? 3 : 5;
	}
}
