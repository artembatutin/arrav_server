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
	KILL_A_MAN("Kill %s times a man", 5, 25),
	BONE_FIRE("Add %s logs to fire pit", 100, 400, 1000, 10_000),
	NO_GUARD("Kill %s Edgevillian guards", 5, 50, 100, 500),
	BURY_BONES("Bury %s bones (any)", 10, 50, 500, 5000),
	VOTE("Redeem %s vote rewards", 1, 10, 25, 50),
	//SETUP_PRELOADING_GEAR("Setup your first gear preset", 1),//TODO
	TRIVIABOT("Answer %s Trivia bot questions", 10, 30, 100, 250),
	HIGH_ALCHEMY("Cast high alchemy spell %s times", 100, 500, 15_000, 50_000),
	BARROWS("Complete %s Barrows runs", 3, 25, 75, 250),
	SKILL_MASTERY("Achieve level 99 in %s skills", 2, 5, 15, 20),
	KILLER("Kill %s players", 10, 50, 150, 1000, 1500, 10_000),
	WOODCUTTING("Chop down %s trees", 100, 500, 1500, 5000),
	MINER("Mine %s rocks", 500, 1500, 5000),
	GRIZZLY_BEAR("Kill 15 grizzly bears", 15, 30, 50, 100),
	BOX_TRAPPER("Trap %s animals in a box", 25, 75, 200, 400),
	FEATHERING("Snare %s birds", 25, 75, 200, 400),
	//SHEAR_30_SHEEPS("Shear %s Sheep", 30, 350, 800),//TODO
	DRAGON_DAGGER("Use %s Dragon dagger specs", 100, 350, 500, 2000),
	//CRYSTAL_CHEST("Open %s crystal chests", 10, 50, 150),//TODO
	//HALL_OF_FAME("Become listed in the Hall of Fame", 1),//TODO
	//AGS_MAX("Hit an 82 with an Armadyl godwsword", 82),//TODO
	//BALLISTA_MAX("Hit an 80 with a Heavy ballista", 80),//TODO
	//MAULED("Kill 75 players with a Granite maul", 75),//TODO
	ENCHANTER("Enchant %s bolts", 1500, 10_000, 30_000),
	//BOSS_DESTROYER("Kill 1,000 of any boss", 1000),//TODO
	POTION_MAKER("Make %s potions with herblore", 200, 500, 4000, 8000);
	
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
	
	public void inc(Player p) {
		AchievementHandler.activate(p, this, 1);
	}
	
	
	public void inc(Player p, int am) {
		AchievementHandler.activate(p, this, am);
	}
}
