package net.edge.content.achievements;

import com.google.common.collect.ImmutableSet;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;

/**
 * Holds all the achievements
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum Achievement {
	//MISC
	CHANGE_APPEARANCE("Change your appearance once", 1),
	SPIDERY("Slash a spider web", 1),
	VOTE("Redeem %s vote rewards", 1, 10, 25, 50),
	DROP_A_SUG("Suggest a monster drop by using the monster database from quest tab", 1, 5, 20, 50),
	TRIVIABOT("Answer %s Trivia bot questions", 10, 30, 100, 250),
	POKER_FLOWER("Plant %s mithril seeds", 3, 10, 20, 50),
	WILD_ONE("Enter the wilderness %s times", 50, 100, 400, 1000),
	TELEPORTER("Teleport %s times around the world of Edgeville", 100, 500, 2500, 10_000),
	//combat
	KILL_A_MAN("Kill %s times a man", 5, 25),
	KILLER("Kill %s players", 10, 50, 150, 1000, 1500, 10_000),
	GRIZZLY_BEAR("Kill 15 grizzly bears", 15, 30, 50, 100),
	DRAGON_DAGGER("Use %s Dragon dagger specs", 100, 350, 500, 2000),
	NO_GUARD("Kill %s Edgevillian guards", 5, 50, 100, 500),
	PKER_OF_THE_WEEK("Win the scoreboard weekly contest", 1, 10, 20, 40),
	//minigames
	HORRORIFIC("Win horroris minigame %s times", 5, 10, 50, 200),
	PEST_CONTROLLER("Win pest control %s times", 5, 10, 50, 200),
	DISASTER("Finish %s recipe of disaster runs", 5, 10, 50, 200),
	ANIMATOR("Kill %s animated armor soldiers", 5, 10, 50, 200),
	BARROWS("Complete %s Barrows runs", 3, 25, 75, 250),
	//skills
	SKILL_MASTERY("Achieve level 99 in %s skills", 2, 5, 15, 20),
	SLAYER_MASTER("Finish %s slayer tasks", 5, 10, 25, 100),
	SHOOT_A_STAR("Finish mining the shooting star", 1, 5, 10, 50),
	MINER("Mine %s rocks", 500, 1500, 5000),
	BONE_FIRE("Add %s logs to fire pit", 100, 400, 1000, 10_000),
	BURY_BONES("Bury %s bones (any)", 10, 50, 500, 5000),
	BOX_TRAPPER("Trap %s animals in a box", 25, 75, 400, 800),
	TOO_FAST("Complete %s agility courses", 25, 75, 400, 800),
	FEATHERING("Snare %s birds", 25, 75, 500, 900),
	BLACKSMITH("Forge %s items", 40, 100, 500, 1000),
	ENCHANTER("Enchant %s bolts", 1500, 10_000, 30_000),
	FISHER_MAN("Catch %s items in the water", 100, 1000, 5000, 10000),
	BON_APETITE("Cook %s items, pizza counts as 20", 100, 500, 1500, 7500),
	FARMER("Harvest %s items from farming", 100, 500, 1000, 4000),
	ARCHER_SUPPORTER("Fletch %s arrows", 50, 500, 1000, 1000),
	LEAVES_AND_STUMPS("Chop down %s trees", 100, 500, 1500, 5000),
	RUNE_CRAFTER("Craft %s runes", 200, 500, 4000, 8000),
	POTION_MAKER("Make %s potions with herblore", 200, 500, 4000, 8000),
	REAL_ROBERY("Steal %s times from stalls", 200, 500, 4000, 8000),
	HIGH_ALCHEMY("Cast high alchemy spell %s times", 100, 500, 15_000, 50_000),
	PRICELESS_GEM("Cut %s gems", 20, 50, 500, 1000),
	GOLD_AND_RIGNS("Create %s jewellery", 20, 50, 100, 500),
	QUALITY_HIDES("Carve %s hide items", 20, 50, 500, 500),
	POTTERY("Create %s pottery items", 20, 50, 500, 5000),;
	
	//COMPLETE_TUTORIAL("Complete the tutorial", 1),//TODO
	//SETUP_PRELOADING_GEAR("Setup your first gear preset", 1),//TODO
	//BOSS_DESTROYER("Kill 1,000 of any boss", 1000),//TODO
	//BOSS_DESTROYER("Kill 1,000 of any boss", 1000),//TODO
	//CRYSTAL_CHEST("Open %s crystal chests", 10, 50, 150),//TODO
	//HALL_OF_FAME("Become listed in the Hall of Fame", 1),//TODO
	//AGS_MAX("Hit an 82 with an Armadyl godwsword", 82),//TODO
	//BALLISTA_MAX("Hit an 80 with a Heavy ballista", 80),//TODO
	//MAULED("Kill 75 players with a Granite maul", 75),//TODO
	
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
