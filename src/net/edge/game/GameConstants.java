package net.edge.game;

import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

/**
 * The class that contains a collection of constants related to the game. This
 * class serves no other purpose than to hold constants.
 * @author lare96 <http://github.org/lare96>
 */
public final class GameConstants {
	
	/**
	 * The public client build number for the login authentication.
	 */
	public static final int CLIENT_BUILD = 21;
	
	/**
	 * The experience mltiplier for the game.
	 */
	public static double EXPERIENCE_MULTIPLIER = 1;
	
	/**
	 * Determines if the double blood money event is active.
	 */
	public static boolean DOUBLE_BLOOD_MONEY_EVENT = false;
	
	/**
	 * How long the player will stay logged in for after they have x-logged
	 * during combat.
	 */
	public static final int LOGOUT_SECONDS = 90;
	
	/**
	 * How long does the player has to wait until he can be considered out
	 * of combat based on his last received hit.
	 */
	public static final int COMBAT_SECONDS = 10;
	
	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGIN_THRESHOLD = 50;
	
	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGOUT_THRESHOLD = 50;
	
	/**
	 * The absolute distance that players must be within to be targeted by
	 * aggressive {@link Npc}s.
	 */
	public static final int TARGET_DISTANCE = 6;
	
	/**
	 * The maximum amount of drops that can be rolled from the dynamic drop
	 * table.
	 */
	public static final int DROP_THRESHOLD = 2;
	
	/**
	 * The time in seconds that has to be spent in a region before {@link Npc}s
	 * stop acting aggressive towards a specific {@link Player}.
	 */
	public static final int TOLERANCE_SECONDS = 600;
	
	/**
	 * The position new players will be moved to.
	 */
	public static final Position STARTING_POSITION = new Position(3088, 3509);
	
	/**
	 * The items that are not allowed to be bought by shops.
	 */
	public static final int[] INVALID_SHOP_ITEMS = {995, 15246, 4067, 2996, 6529, 19000, 7478};
	
	/**
	 * The message that will be sent on every login.
	 */
	public static final String WELCOME_MESSAGE = "@blu@Welcome to Edgeville - Report any bugs on the forums when found.";
	
	/**
	 * The items received when a player logs in for the first time.
	 */
	public static final Item STARTER_PACKAGE[] = {new Item(995, 500000), new Item(19000, 50), new Item(362, 200), new Item(1351), new Item(590), new Item(1265), new Item(946), new Item(1323), new Item(841), new Item(1379), new Item(3105), new Item(1153), new Item(1115), new Item(1067), new Item(1191), new Item(579), new Item(577), new Item(1011), new Item(4405), new Item(884, 500), new Item(554, 250), new Item(555, 250), new Item(556, 250), new Item(557, 250), new Item(558, 250), new Item(560, 150), new Item(562, 250)};
	
	/**
	 * Messages chosen a random to be sent to a player that has killed another
	 * player. {@code -victim-} is replaced with the player's name that was
	 * killed, while {@code -killer-} is replaced with the killer's name.
	 */
	public static final String[] DEATH_MESSAGES = {"You have just killed -victim-!", "You have completely slaughtered -victim-!", "I bet -victim- will think twice before messing with you again!", "Your killing style is impeccable, -victim- didn't stand a chance!"};

	/**
	 * Strings that can not be used in a username.
	 */
	public static final String BAD_USERNAMES[] = {"m o d", "a d m i n", "mod", "admin", "moderator", "administrator", "owner", "m0d", "adm1n", "0wner", "retard", "Nigga", "nigger", "n1gger", "n1gg3r", "nigg3r", "n1gga", "cock", "faggot", "fag", "anus", "arse", "fuck", "bastard", "bitch", "cunt", "chode", "damn", "dick", "faggit", "gay", "homo", "jizz", "lesbian", "negro", "pussy", "penis", "queef", "twat", "titty", "whore", "b1tch"};

	/**
	 * Strings that are classified as bad.
	 */
	public static final String[] BAD_STRINGS = {
			"fag", "f4g", "faggot", "nigger", "fuck", "bitch", "whore", "slut",
			"gay", "lesbian", "scape", ".net", ".org", "vagina", "dick",
			"cock", "penis", "hoe", "soulsplit", "ikov", "retard", "cunt"
	};

	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private GameConstants() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
}
