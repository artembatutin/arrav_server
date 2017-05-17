package net.edge.world;

import net.edge.net.database.Database;
import net.edge.net.database.pool.ConnectionPool;
import net.edge.task.Task;
import net.edge.task.TaskManager;
import net.edge.utils.LoggerUtils;
import net.edge.utils.Stopwatch;
import net.edge.utils.log.LoggingManager;
import net.edge.world.content.PlayerPanel;
import net.edge.world.content.clanchat.ClanManager;
import net.edge.world.content.container.session.ExchangeSessionManager;
import net.edge.world.content.scoreboard.ScoreboardManager;
import net.edge.world.content.shootingstar.ShootingStarManager;
import net.edge.world.content.skill.firemaking.pits.FirepitManager;
import net.edge.world.model.locale.InstanceManager;
import net.edge.world.model.locale.area.AreaManager;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.EntityList;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.move.path.AStarPathFinder;
import net.edge.world.model.node.entity.move.path.Distance;
import net.edge.world.model.node.entity.move.path.SimplePathChecker;
import net.edge.world.model.node.entity.move.path.SimplePathFinder;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.npc.NpcMovementTask;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.region.RegionManager;
import net.edge.world.model.node.region.TraversalMap;
import net.edge.world.model.node.synchronizer.WorldSynchronizer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The static utility class that contains functions to manage and process game characters.
 * @author lare96 <http://github.com/lare96>
 */
public final class World {
	
	/**
	 * The logger that will print important information.
	 */
	private static Logger logger = LoggerUtils.getLogger(World.class);
	
	/**
	 * The game service that processes this world.
	 */
	private static final GameService SERVICE = new GameService();
	
	/**
	 * The manager for the queue of game tasks.
	 */
	private static final TaskManager TASK_MANAGER = new TaskManager();
	
	/**
	 * The {@link WorldSynchronizer} that will perform updating for all {@link EntityNode}s.
	 */
	private static final WorldSynchronizer SYNCHRONIZER = new WorldSynchronizer();
	
	/**
	 * The collection of active players.
	 */
	private static EntityList<Player> players = new EntityList<>(2048);
	
	/**
	 * The collection of active NPCs.
	 */
	private static EntityList<Npc> npcs = new EntityList<>(16384);
	
	/**
	 * The queue of {@link Player}s waiting to be logged in.
	 */
	private static Queue<Player> logins = new ConcurrentLinkedQueue<>();
	
	/**
	 * The queue of {@link Player}s waiting to be logged out.
	 */
	private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();
	
	/**
	 * The time it took in milliseconds to do the sync.
	 */
	public static long millis;
	
	static {
		try {
			//donation = new Database("45.32.7.163", "avarrocka_donate", "avarrocka", "@$p{eh2FS5A8%b6k", 4);
			//score = new Database("45.32.7.163", "avarrocka_score", "avarrocka", "@$p{eh2FS5A8%b6k", 1);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The default constructor, will throw an
	 * {@link UnsupportedOperationException} if instantiated.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private World() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
	
	/**
	 * The method that executes the update sequence for all in game characters
	 * every cycle. The update sequence may either run sequentially or
	 * concurrently depending on the type of engine selected by the server.
	 * @throws Exception if any errors occur during the update sequence.
	 */
	public static void sequence() throws Exception {
		long start = System.currentTimeMillis();
		
		// Handle queued logins.
		for(int amount = 0; amount < GameConstants.LOGIN_THRESHOLD; amount++) {
			Player player = logins.poll();
			if(player == null)
				break;
			if(!players.add(player)) {
				player.getSession().getChannel().close();
			}
		}
		
		// Handle task processing.
		TASK_MANAGER.sequence();
		
		//Parsing a global sync.
		SYNCHRONIZER.preSynchronize();
		SYNCHRONIZER.synchronize();
		SYNCHRONIZER.postSynchronize();
		
		// Handle queued logouts.
		int amount = 0;
		Iterator<Player> $it = logouts.iterator();
		while($it.hasNext()) {
			Player player = $it.next();
			if(player == null || amount >= GameConstants.LOGOUT_THRESHOLD)
				break;
			if(handleLogout(player)) {
				$it.remove();
				amount++;
			}
		}
		
		millis = System.currentTimeMillis() - start;
	}
	
	/**
	 * Queues {@code player} to be logged in on the next server sequence.
	 * @param player the player to log in.
	 */
	public static void queueLogin(Player player) {
		if(!logins.contains(player)) {
			logins.add(player);
		}
	}
	
	/**
	 * Queues {@code player} to be logged out on the next server sequence.
	 * @param player the player to log out.
	 */
	public static void queueLogout(Player player) {
		if(player.getState() == NodeState.ACTIVE && !logouts.contains(player)) {
			if(player.getCombatBuilder().inCombat())
				player.getLogoutTimer().reset();
			logouts.add(player);
		}
	}
	
	/**
	 * Submits {@code t} to the backing {@link TaskManager}.
	 * @param t the task to submit to the queue.
	 */
	public static void submit(Task t) {
		TASK_MANAGER.submit(t);
	}
	
	/**
	 * Returns a player within an optional whose name hash is equal to
	 * {@code username}.
	 * @param username the name hash to check the collection of players for.
	 * @return the player within an optional if found, or an empty optional if
	 * not found.
	 */
	public static Optional<Player> getPlayer(long username) {
		return players.findFirst(it -> Objects.equals(it.getUsernameHash(), username));
	}
	
	/**
	 * Returns a player within an optional whose name is equal to
	 * {@code username}.
	 * @param username the name to check the collection of players for.
	 * @return the player within an optional if found, or an empty optional if
	 * not found.
	 */
	public static Optional<Player> getPlayer(String username) {
		return players.findFirst(it -> Objects.equals(it.getUsername(), username));
	}
	
	/**
	 * Retrieves and returns the local {@link Player}s for {@code character}.
	 * The specific players returned is completely dependent on the character
	 * given in the argument.
	 * @param character the character that it will be returned for.
	 * @return the local players.
	 */
	public static Iterator<Player> getLocalPlayers(EntityNode character) {
		if(character.isPlayer())
			return character.toPlayer().getLocalPlayers().iterator();
		return players.iterator();
	}
	
	/**
	 * Retrieves and returns the local {@link Npc}s for {@code character}. The
	 * specific npcs returned is completely dependent on the character given in
	 * the argument.
	 * @param character the character that it will be returned for.
	 * @return the local npcs.
	 */
	public static Iterator<Npc> getLocalNpcs(EntityNode character) {
		if(character.isPlayer())
			return character.toPlayer().getLocalNpcs().iterator();
		return npcs.iterator();
	}
	
	/**
	 * Gets every single character in the player and npc character lists.
	 * @return a set containing every single character.
	 */
	public static Set<EntityNode> getEntities() {
		Set<EntityNode> characters = new HashSet<>();
		players.forEach(characters::add);
		npcs.forEach(characters::add);
		return characters;
	}
	
	/**
	 * Sends {@code message} to all online players with an announcement dependent of {@code announcement}.
	 * @param message      the message to send to all online players.
	 * @param announcement determines if this message is an announcement.
	 */
	public static void message(String message, boolean announcement) {
		players.forEach(p -> p.message((announcement ? "@red@[ANNOUNCEMENT]: " : "") + message));
	}
	
	/**
	 * Sends {@code message} to all online players without an announcement.
	 * @param message the message to send to all online players.
	 */
	public static void message(String message) {
		message(message, false);
	}
	
	/**
	 * Performs all of the disconnection logic for {@code player} assuming they
	 * are in the logout queue.
	 * @param player the player to attempt to logout.
	 * @return {@code true} if the player was logged out, {@code false}
	 * otherwise.
	 */
	public static boolean handleLogout(Player player) {
		try {
			// If the player x-logged, don't log the player out. Keep the
			// player queued until they are out of combat to prevent x-logging.
			//if(!forced) {
			if(!player.getLogoutTimer().elapsed(GameConstants.LOGOUT_SECONDS, TimeUnit.SECONDS) && (player.getCombatBuilder().isBeingAttacked() && player.getCombatBuilder().pjingCheck())) {
				return false;
			}
			//}
			boolean response = World.getPlayers().remove(player);
			PlayerPanel.PLAYERS_ONLINE.refreshAll("@or2@ - Players online: @yel@" + World.getPlayers().size());
			List<Npc> npcs = World.getNpcs().findAll(n -> n != null && n.isSpawnedFor(player));
			for(Npc n : npcs) {
				World.getNpcs().remove(n);
			}
			if(response)
				logger.info(player.toString() + " has logged out.");
			return response;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the collection of active players.
	 * @return the active players.
	 */
	public static EntityList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets the collection of active npcs.
	 * @return the active npcs.
	 */
	public static EntityList<Npc> getNpcs() {
		return npcs;
	}
	
	/**
	 * Returns the game service that processes this world.
	 * @return the game service.
	 */
	public static GameService getService() {
		return SERVICE;
	}
	
	/**
	 * Gets the manager for the queue of game tasks.
	 * @return the queue of tasks.
	 */
	public static TaskManager getTaskManager() {
		return TASK_MANAGER;
	}




	/* FINAL STATIC ASSETS DECLARATION */
	
	/**
	 * The time the server has been running.
	 */
	private static final Stopwatch RUNNING_TIME = new Stopwatch().reset();
	
	/**
	 * The shooting star event for the world.
	 */
	private static final ShootingStarManager SHOOTING_STAR_MANAGER = new ShootingStarManager();
	
	/**
	 * The fire pit event for the world.
	 */
	private static final FirepitManager FIRE_PIT_EVENT = new FirepitManager();
	
	/**
	 * The clan manager for the world.
	 */
	private static final ClanManager CLAN_MANAGER = new ClanManager();
	
	/**
	 * The instance manager for the world.
	 */
	private static final InstanceManager INSTANCE_MANAGER = new InstanceManager();
	
	/**
	 * The {@link RegionManager} that manages region caching.
	 */
	private static final RegionManager REGION_MANAGER = new RegionManager();
	
	/**
	 * This world's {@link TraversalMap} used to handle the clipping around the world.
	 */
	private static final TraversalMap TRAVERSAL_MAP = new TraversalMap();
	
	/**
	 * This world's {@link AreaManager} used to handle to check if certain areas have permissions.
	 */
	private static final AreaManager AREA_MANAGER = new AreaManager();
	
	/**
	 * The world's A* path finder use for player's movements.
	 */
	private static final AStarPathFinder A_STAR_PATH_FINDER = new AStarPathFinder(TRAVERSAL_MAP, new Distance.Manhattan());
	
	/**
	 * This world's straight line pathfinder used for NPCs movements.
	 */
	private static final SimplePathFinder SIMPLE_PATH_FINDER = new SimplePathFinder(TRAVERSAL_MAP);
	
	/**
	 * This world's straight line path checker for combat.
	 */
	private static final SimplePathChecker SIMPLE_PATH_CHECKER = new SimplePathChecker(TRAVERSAL_MAP);
	
	/**
	 * This world's {@link ExchangeSessionManager} used to handle container sessions.
	 */
	private static final ExchangeSessionManager EXCHANGE_SESSION_MANAGER = new ExchangeSessionManager();
	
	/**
	 * This world's {@link LoggingManager} used to log player actions.
	 */
	private static final LoggingManager LOG_MANAGER = new LoggingManager();
	
	/**
	 * This world's {@link ScoreboardManager} used to check scores.
	 */
	private static final ScoreboardManager SCOREBOARD_MANAGER = new ScoreboardManager();
	
	/**
	 * A integral {@link Task} that handles ranomized movement of all {@link Npc}s.
	 */
	private static final NpcMovementTask NPC_MOVEMENT_TASK = new NpcMovementTask();
	
	/**
	 * The scores database connection.
	 */
	private static Database score;
	
	/**
	 * The donation database connection.
	 */
	private static Database donation;
	
	
	
	
	/* ASSETS GATHERS METHODS. */
	
	/**
	 * Returns this world's {@link ExchangeSessionManager}.
	 */
	public static ExchangeSessionManager getExchangeSessionManager() {
		return EXCHANGE_SESSION_MANAGER;
	}
	
	/**
	 * Returns this world's {@link LoggingManager}.
	 */
	public static LoggingManager getLoggingManager() {
		return LOG_MANAGER;
	}
	
	/**
	 * Returns this world's {@link ScoreboardManager}.
	 */
	public static ScoreboardManager getScoreboardManager() {
		return SCOREBOARD_MANAGER;
	}
	
	/**
	 * Returns the running time {@link Stopwatch}.
	 */
	public static Stopwatch getRunningTime() {
		return RUNNING_TIME;
	}
	
	/**
	 * Returns the fire pit event manager.
	 */
	public static FirepitManager getFirepitEvent() {
		return FIRE_PIT_EVENT;
	}
	
	/**
	 * Returns the shooting star event manager.
	 */
	public static ShootingStarManager getShootingStarEvent() {
		return SHOOTING_STAR_MANAGER;
	}
	
	/**
	 * Returns the clan chat manager.
	 */
	public static ClanManager getClanManager() {
		return CLAN_MANAGER;
	}
	
	/**
	 * Returns the instance manager.
	 */
	public static InstanceManager getInstanceManager() {
		return INSTANCE_MANAGER;
	}
	
	/**
	 * Returns this world's {@link RegionManager}.
	 */
	public static RegionManager getRegions() {
		return REGION_MANAGER;
	}
	
	/**
	 * Returns this world's {@link TraversalMap}.
	 */
	public static TraversalMap getTraversalMap() {
		return TRAVERSAL_MAP;
	}
	
	/**
	 * Returns this world'{@link AreaManager}.
	 */
	public static AreaManager getAreaManager() {
		return AREA_MANAGER;
	}
	
	/**
	 * Returns this world's A* pathfinder.
	 */
	public static AStarPathFinder getAStarPathFinder() {
		return A_STAR_PATH_FINDER;
	}
	
	/**
	 * Returns this world's straight pathfinder.
	 */
	public static SimplePathFinder getSimplePathFinder() {
		return SIMPLE_PATH_FINDER;
	}
	
	/**
	 * Returns this world's straight path checker.
	 */
	public static SimplePathChecker getSimplePathChecker() {
		return SIMPLE_PATH_CHECKER;
	}
	
	/**
	 * Returns the randomized npc task.
	 */
	public static NpcMovementTask getNpcMovementTask() {
		return NPC_MOVEMENT_TASK;
	}
	
	/**
	 * Gets the highscores database connection pool.
	 */
	public static ConnectionPool getScore() {
		if(score == null)
			return null;
		return score.getPool();
	}
	
	/**
	 * Gets the donation database connection pool.
	 */
	public static Connection getDonation() {
		if(donation == null)
			return null;
		try {
			return donation.getConnection();
		} catch(SQLException e) {
			return null;
		}
	}
}