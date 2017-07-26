package net.edge.content.achievements;

import com.google.common.collect.ImmutableSet;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;

/**
 * Holds all the achievements
 * @author Daniel
 */
public enum Achievement {
	
	COMPLETE_TUTORIAL("Complete the tutorial", 1),//TODO
	CHANGE_APPEARANCE("Change your appearance once", 1),
	KILL_A_MAN("Kill a man", 1),//TODO
	BURY_BONES("Bury %s bones (any)", 10, 50, 500, 5000),
	VOTE("Claim %s vote rewards", 1, 10, 25, 50),//TODO
	SETUP_PRELOADING_GEAR("Setup your first gear preset", 1),//TODO
	TRIVIABOT("Answer %s TriviaBot questions", 10, 30, 100, 250),//TODO
	HIGH_ALCHEMY("Cast high alchemy spell 100 times", 100),//TODO
	BARROWS("Complete %s Barrows runs", 3, 25, 75, 250),//TODO
	SKILL_MASTERY("Achieve level 99 in %s skill", 1, 3, 5),//TODO
	KILLER("Kill %s players", 10, 50, 150, 1000, 1500, 10_000),//TODO
	WOODCUTTING("Chop down %s trees", 100, 500, 1500, 5000),//TODO
	MINER("Mine %s rocks", 500, 1500, 5000),//TODO
	GRIZZLY_BEAR("Kill 15 grizzly bears", 15, 15_000),//TODO
	SHEAR_30_SHEEPS("Shear %s Sheep", 30, 350, 800),//TODO
	DAGGER("Use 100 Dragon dagger specs", 100),//TODO
	CRYSTAL_CHEST("Open %s crystal chests", 10, 50, 150),//TODO
	HALL_OF_FAME("Become listed in the Hall of Fame", 1),//TODO
	AGS_MAX("Hit an 82 with an Armadyl godwsword", 82),//TODO
	BALLISTA_MAX("Hit an 80 with a Heavy ballista", 80),//TODO
	MAULED("Kill 75 players with a Granite maul", 75),//TODO
	ENCHANT("Enchant %s bolts", 1500, 10_000),//TODO
	BOSS_DESTROYER("Kill 1,000 of any boss", 1000),//TODO
	MAKE_10000_POTIONS("Make 10,000 potions with herblore", 10000);//TODO
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<Achievement> VALUES = ImmutableSet.copyOf(values());
	
	/**
	 * The amount required to complete the achievement.
	 */
	private final int[] amount;
	
	/**
	 * The achievement task string.
	 */
	private final String task;
	
	/**
	 * The name of this achievement
	 */
	private final String name;
	
	/**
	 * Constructs a new <code>Achievement<code>.
	 * @param amount The amount required to complete the achievement.
	 * @param task   The achievement task string.
	 */
	Achievement(String task, int... amount) {
		this.amount = amount;
		this.task = task;
		this.name = TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
	}
	
	/**
	 * Gets the amount required to complete the achievement.
	 * @return Achievement amount.
	 */
	public int[] getAmount() {
		return amount;
	}
	
	/**
	 * Gets the achievement task.
	 * @return Achievement task.
	 */
	public String getTask() {
		return task;
	}
	
	/**
	 * Gets the achievement name.
	 * @return Achievement name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the total amount of achievements.
	 * @return Achievement total.
	 */
	public static int getTotal() {
		return values().length;
	}
	
	public void inc(Player p, int am) {
		AchievementHandler.activate(p, this, am);
	}
}
