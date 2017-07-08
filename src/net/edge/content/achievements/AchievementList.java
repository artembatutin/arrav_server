package net.edge.content.achievements;

import com.google.common.collect.ImmutableSet;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds all the achievements
 * 
 * @author Daniel
 */
public enum AchievementList {

	/* Easy achievements */
	COMPLETE_TUTORIAL(1, AchievementKey.COMPLETE_TUTORIAL, AchievementDifficulty.EASY, "Complete the tutorial"), // TODO
	CHANGE_APPEARANCE(1, AchievementKey.CHANGE_APPEARANCE, AchievementDifficulty.EASY, "Change your appearance once"),
	KILL_A_MAN(1, AchievementKey.KILL_A_MAN, AchievementDifficulty.EASY, "Kill a man"),
	BURY_BONES(10, AchievementKey.BURY_BONES, AchievementDifficulty.EASY, "Bury 10 bones (any)"),
	VOTE(1, AchievementKey.VOTE, AchievementDifficulty.EASY, "Claim 1 vote reward"), // TODO
	SETUP_PRELOADING_GEAR(1, AchievementKey.SETUP_PRELOADING_GEAR, AchievementDifficulty.EASY, "Setup your first gear preset"),
	TRIVIABOT(10, AchievementKey.TRIVIABOT, AchievementDifficulty.EASY, "Answer 10 TriviaBot questions"),
	HIGH_ALCHEMY(100, AchievementKey.HIGH_ALCHEMY, AchievementDifficulty.EASY, "Cast high alchemy spell 100 times"),
	BARROWS(3, AchievementKey.BARROWS, AchievementDifficulty.EASY, "Complete 3 Barrows runs"), // TODO
	SKILL_MASTERY(1, AchievementKey.SKILL_MASTERY, AchievementDifficulty.EASY, "Achieve level 99 in 1 skill"), // TODO
	KILLER(10, AchievementKey.KILLER, AchievementDifficulty.EASY, "Kill 10 players"),
	WOODCUTTING(100, AchievementKey.WOODCUTTING, AchievementDifficulty.EASY, "Chop down 100 trees"),
	MINER(100, AchievementKey.MINER, AchievementDifficulty.EASY, "Mine 100 rocks"), // TODO
	CRYSTAL_CHEST(10, AchievementKey.CRYSTAL_CHEST, AchievementDifficulty.EASY, "Open 10 crystal chests"),
	GRIZZLY_BEAR(15, AchievementKey.GRIZZLY_BEAR, AchievementDifficulty.EASY, "Kill 15 grizzly bears"),
	SHEAR_30_SHEEPS(30, AchievementKey.SHEAR_SHEEPS, AchievementDifficulty.EASY, "Shear 30 Sheeps"),// TODO

	/* Medium achievements */
	SKILL_MASTERY_I(3, AchievementKey.SKILL_MASTERY, AchievementDifficulty.MEDIUM, "Achieve level 99 in 3 skills"),
	VOTE_I(10, AchievementKey.VOTE, AchievementDifficulty.MEDIUM, "Claim 10 vote rewards"), // TODO
	TRIVIABOT_I(30, AchievementKey.TRIVIABOT, AchievementDifficulty.MEDIUM, "Answer 30 TriviaBot questions"),
	BARROWS_I(25, AchievementKey.BARROWS, AchievementDifficulty.MEDIUM, "Complete 50 Barrows runs"), // TODO
	KILLER_I(50, AchievementKey.KILLER, AchievementDifficulty.MEDIUM, "Kill 50 players"),
	DAGGER_I(100, AchievementKey.DAGGER, AchievementDifficulty.MEDIUM, "Use 100 Dragon dagger specs"), //TODO
	WOODCUTTING_I(500, AchievementKey.WOODCUTTING, AchievementDifficulty.MEDIUM, "Chop down 500 trees"),
	MINER_I(500, AchievementKey.MINER, AchievementDifficulty.MEDIUM, "Mine 500 rocks"), // TODO
	CRYSTAL_CHEST_I(50, AchievementKey.CRYSTAL_CHEST, AchievementDifficulty.MEDIUM, "Open 50 crystal chests"),
	SHEAR_350_SHEEPS(350, AchievementKey.SHEAR_SHEEPS, AchievementDifficulty.MEDIUM, "Shear 350 Sheeps"),// TODO

	/* Hard achievements */
	EXPERIENCE_MASTERY(5, AchievementKey.EXPERIENCE_MASTERY, AchievementDifficulty.HARD, "Earn 200M EXP in 5 skills"),
	VOTE_II(25, AchievementKey.VOTE, AchievementDifficulty.HARD, "Claim 25 vote rewards"), // TODO
	TRIVIABOT_II(100, AchievementKey.TRIVIABOT, AchievementDifficulty.HARD, "Answer 100 TriviaBot questions"),
	BARROWS_II(75, AchievementKey.BARROWS, AchievementDifficulty.HARD, "Complete 100 Barrows runs"), // TODO
	KILLER_II(150, AchievementKey.KILLER, AchievementDifficulty.HARD, "Kill 150 players"),
	WOODCUTTING_II(1500, AchievementKey.WOODCUTTING, AchievementDifficulty.HARD, "Chop down 1,500 trees"),
	MINER_II(1500, AchievementKey.MINER, AchievementDifficulty.HARD, "Mine 1500 rocks"), // TODO
	HALL_OF_FAME(1, AchievementKey.HALL_OF_FAME, AchievementDifficulty.HARD, "Become listed in the Hall of Fame"), // TODO
	AGS_MAX(82, AchievementKey.AGS_MAX, AchievementDifficulty.HARD, "Hit an 82 with an Armadyl godwsword"), // TODO
	BALLISTA_MAX(8, AchievementKey.BALLISTA_MAX, AchievementDifficulty.HARD, "Hit an 80 with a Heavy ballista"), // TODO
	CRYSTAL_CHEST_II(150, AchievementKey.CRYSTAL_CHEST, AchievementDifficulty.HARD, "Open 150 crystal chests"),
	MAULED(75, AchievementKey.MAULED, AchievementDifficulty.HARD, "Kill 75 players with a Granite maul"), // TODO
	SHEAR_800_SHEEPS(800, AchievementKey.SHEAR_SHEEPS, AchievementDifficulty.HARD, "Shear 800 Sheeps"),// TODO
	ENCHANT_1500_BOLTS(1500, AchievementKey.ENCHANT_BOLTS, AchievementDifficulty.HARD, "Enchant 1,500 bolts"), //TODO

	/* Elite achievements */
	VOTE_III(50, AchievementKey.VOTE, AchievementDifficulty.ELITE, "Claim 50 vote rewards"), // TODO
	TRIVIABOT_III(250, AchievementKey.TRIVIABOT, AchievementDifficulty.ELITE, "Answer 300 TriviaBot questions"),
	BARROWS_III(250, AchievementKey.BARROWS, AchievementDifficulty.ELITE, "Complete 500 Barrows runs"), // TODO
	KILLER_III(1000, AchievementKey.KILLER, AchievementDifficulty.ELITE, "Kill 1,000 players"),
	KILLER_IV(10000, AchievementKey.KILLER, AchievementDifficulty.ELITE, "Kill 10,000 players"),
	WOODCUTTING_III(5000, AchievementKey.WOODCUTTING, AchievementDifficulty.ELITE, "Chop down 5,000 trees"),
	MINER_III(5000, AchievementKey.MINER, AchievementDifficulty.ELITE, "Mine 5,000 rocks"), // TODO
	CRYSTAL_CHEST_III(500, AchievementKey.CRYSTAL_CHEST, AchievementDifficulty.ELITE, "Open 500 crystal chests"),
	BOSS_DESTROYER(1000, AchievementKey.BOSS_DESTROYER, AchievementDifficulty.ELITE, "Kill 1,000 of any boss"), //TODO
	BILLIONS(1, AchievementKey.BILLIONS, AchievementDifficulty.ELITE, "Have a bank worth of 2B"), //TODO
	GRIZZLY_BEAR_I(15_000, AchievementKey.GRIZZLY_BEAR, AchievementDifficulty.ELITE, "Kill 15,000 grizzly bears"),
	ENCHANT_10000_BOLTS(10000, AchievementKey.ENCHANT_BOLTS, AchievementDifficulty.ELITE, "Enchant 10,000 bolts"), //TODO
	MAKE_10000_POTIONS(10000, AchievementKey.POTION_MAKING, AchievementDifficulty.ELITE, "Make 10,000 potions with herblore")

	;

	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<AchievementList> VALUES = ImmutableSet.copyOf(values());

	/** The amount required to complete the achievement. */
	private final int amount;

	/** The key of this achievement */
	private final AchievementKey key;

	/** The achievement difficulty. */
	private final AchievementDifficulty difficulty;

	/* The achievement task string. */
	private final String task;

	/**
	 * Constructs a new <code>AchievementList<code>.
	 * 
	 * @param amount
	 *            The amount required to complete the achievement.
	 * @param key
	 * 			  The key this achievement belongs to.
	 * @param difficulty
	 *            The achievement difficulty.
	 * @param task
	 *            The achievement task string.
	 */
	AchievementList(int amount, AchievementKey key, AchievementDifficulty difficulty, String task) {
		this.amount = amount;
		this.key = key;
		this.difficulty = difficulty;
		this.task = task;
	}

	/**
	 * Gets the amount required to complete the achievement.
	 * 
	 * @return Achievement amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the achievement key.
	 * @return the achievement key.
	 */
	public AchievementKey getKey() {
		return key;
	}

	/**
	 * Gets the achievement difficulty.
	 * 
	 * @return Achievement difficulty.
	 */
	public AchievementDifficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * Gets the achievement task.
	 * 
	 * @return Achievement task.
	 */
	public String getTask() {
		return task;
	}

	/**
	 * Gets the achievements as a list.
	 * 
	 * @param difficulty
	 *            The achievement difficulty to filter.
	 * @return The list of achievements.
	 */
	public static List<AchievementList> asList(AchievementDifficulty difficulty) {
		return VALUES.stream().filter(a -> a.getDifficulty() == difficulty).sorted(Comparator.comparing(Enum::name)).collect(Collectors.toList());
	}

	/**
	 * Gets the total amount of achievements.
	 * 
	 * @return Achievement total.
	 */
	public static int getTotal() {
		return values().length;
	}
}
