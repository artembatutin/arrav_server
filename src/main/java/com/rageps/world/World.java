package com.rageps.world;

import com.rageps.GameConstants;
import com.rageps.RagePS;
import com.rageps.command.impl.UpdateCommand;
import com.rageps.content.PlayerPanel;
import com.rageps.net.discord.Discord;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.net.refactor.release.Release;
import com.rageps.net.refactor.release.Release317;
import com.rageps.net.sql.DatabaseTransactionWorker;
import com.rageps.service.ServiceManager;
import com.rageps.service.impl.GameService;
import com.rageps.service.impl.LoginService;
import com.rageps.task.Task;
import com.rageps.task.TaskManager;
import com.rageps.util.Stopwatch;
import com.rageps.util.log.LoggingManager;
import com.rageps.world.attr.AttributeMap;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.ActorList;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobMovementTask;
import com.rageps.world.entity.actor.move.path.AStarPathFinder;
import com.rageps.world.entity.actor.move.path.SimplePathChecker;
import com.rageps.world.entity.actor.move.path.impl.SimplePathFinder;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.Region;
import com.rageps.world.entity.region.RegionManager;
import com.rageps.world.env.Environment;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static com.rageps.world.entity.EntityState.AWAITING_REMOVAL;

/**
 * The static utility class that contains functions to manage and process game characters.
 * @author Artem Batutin
 */
public final class World {

	/**
	 * Pulses this World.
	 */
	public void pulse() {
		//unregisterNpcs();
		//registerNpcs();
		taskManager.sequence();
	}


	/**
	 * Queues {@code player} to be logged in on the next server sequence.
	 *
	 * @Deprectated {@link LoginService}.
	 *
	 * @param player the player to log in.
	 */
	/*public void queueLogin(Player player) {
		if(player.getState() == IDLE) {
			logins.offer(player);
		}
	}*/
	
	/**
	 * Dequeue all incoming connecting players.
	 *
	 * @Deprecated {@link LoginService}.
	 */
	/*public void dequeueLogins() {
		if(!logins.isEmpty()) {
			for(int i = 0; i < GameConstants.LOGIN_THRESHOLD; i++) {
				Player player = logins.poll();
				if(player == null) {
					break;
				}
				if(players.remaining() < 1) {
					player.getSession().getChannel().close();
					continue;
				}
				if(playerByNames.containsKey(player.credentials.usernameHash)) {
					player.getSession().getChannel().close();
				} else if(players.add(player)) {
					playerByNames.put(player.credentials.usernameHash, player);
				} else {
					player.getSession().getChannel().close();
				}
			}
			PlayerPanel.PLAYERS_ONLINE.refreshAll("@or2@ - Players online: @yel@" + players.size());
		}
	}*/
	
	/**
	 * Queues {@code actor} to be added to the world.
	 * @param actor the actor adding.
	 */
	public void add(Actor actor) {
		registeringActors.add(actor);
	}
	
	/**
	 * Dequeue all new registered mobs.
	 *
	 */
	public void registerActors() {
		if(!registeringActors.isEmpty()) {
			for(int i = 0; i < registeringActors.size(); i++) {
				Actor actor = registeringActors.poll();
				if(actor == null) {
					break;
				}
				Region reg = actor.getRegion();
				if(reg != null) {
					reg.add(actor);
				}
			}
		}
	}

	/**
	 * Queues {@code actor} to be removed to the world.
	 * @param actor the actor removing.
	 */
	public void remove(Actor actor) {
		disposingActors.add(actor);
	}
	

	/**
	 * Dequeue all old disposed mobs.
	 */
	public void disposeActors() {
		if(!disposingActors.isEmpty()) {
			for(int i = 0; i < disposingActors.size(); i++) {
				Actor actor = disposingActors.poll();
				if(actor == null) {
					break;
				}
				Region reg = actor.getRegion();
				if(reg != null) {
					reg.remove(actor);
				}
			}
		}
	}

	/**
	 * Queues {@code player} to be logged out on the next server sequence.
	 * @param player the player to log out.
	 *
	 * @deprecated {@link LoginService}.
	 *
	 */
	public void queueLogout(Player player) {
		if(player.getCombat().inCombat())
			player.getLogoutTimer().reset();
		player.setState(AWAITING_REMOVAL);
		player.send(new LogoutPacket(player));
		logouts.add(player);
	}
	
	/**
	 * Dequeue the logged out demands.
	 *
	 * @deprecated {@link LoginService}.
	 *
	 */
	public void dequeueLogout() {
		if(!logouts.isEmpty()) {
			for(int i = 0; i < GameConstants.LOGOUT_THRESHOLD; i++) {
				Player player = logouts.poll();
				if(player == null) {
					continue;
				}
				if(!logout(player)) {
					logouts.offer(player);
				}
			}
			PlayerPanel.PLAYERS_ONLINE.refreshAll("@or2@ - Players online: @yel@" + players.size());
		}
	}
	
	/**
	 * Submits {@code t} to the backing {@link TaskManager}.
	 * @param t the task to submit to the queue.
	 */
	public void submit(Task t) {
		taskManager.submit(t);
	}
	
	/**
	 * Performs all of the disconnection logic for {@code player} assuming they
	 * are in the logout queue.
	 * @param player the player to attempt to logout.
	 * @return {@code true} if the player was logged out, {@code false}
	 * otherwise.
	 */
	public boolean logout(Player player) {
		try {
			// If the player x-logged, don't log the player out. Keep the
			// player queued until they are out of combat to prevent x-logging.
			if(player.getLogoutTimer().elapsed(GameConstants.LOGOUT_SECONDS, TimeUnit.SECONDS) && player.getCombat().isUnderAttack() && UpdateCommand.inProgess != 2) {
				return false;
			}
			boolean response = players.remove(player);
			for(Mob mob : player.getMobs()) {
				mobRepository.remove(mob);
			}
			player.getMobs().clear();
			if(response) {
				System.out.println(player + " logged out.");
				playerByNames.remove(player.credentials.usernameHash);
			}
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public World(Environment environment) {
		World.environment = environment;
		services = new ServiceManager(this);

		if(environment.getType() == Environment.Type.LIVE)
			discord = new Discord();

		if(environment.isSqlEnabled())
			databaseWorker = new DatabaseTransactionWorker();

	}

	/* CONSTANTS DECLARATIONS */

	/**
	 * The LOGGER that will print important information.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * An implementation of the singleton pattern to prevent indirect
	 * instantiation of this class file.
	 */
	private static final World singleton = RagePS.world;

	/**
	 * The release relating to this world.
	 */
	private static final Release release = new Release317();

	/**
	 * An environment provided with configuration related to the specific
	 * environment the server is running on.
	 */
	private static Environment environment;

	/**
	 * This Discord for this World
	 */
	private static Discord discord;

	/**
	 * The service manager.
	 */
	private final ServiceManager services;

	/**
	 * Responsible for asynchronously executing all database transactions.
	 */
	private DatabaseTransactionWorker databaseWorker;

	/**
	 * World attributes.
	 */
	private static final AttributeMap attributeMap = new AttributeMap();

	/**
	 * The manager for the queue of game tasks.
	 */
	private final TaskManager taskManager = new TaskManager();

	/**
	 * The queue of {@link Player}s waiting to be logged in.
	 */
	private final Queue<Player> logins = new ConcurrentLinkedQueue<>();

	/**
	 * The queue of {@link Player}s waiting to be logged out.
	 */
	private final Queue<Player> logouts = new ConcurrentLinkedQueue<>();

	/**
	 * The queue of {@link Actor}s waiting to be added to the world.
	 */
	private final Queue<Actor> registeringActors = new ConcurrentLinkedQueue<>();

	/**
	 * The queue of {@link Actor}s waiting to be removed from the world.
	 */
	private final Queue<Actor> disposingActors = new ConcurrentLinkedQueue<>();


	/**
	 * The collection of active NPCs.
	 */
	private final ActorList<Mob> mobRepository = new ActorList<>(16384);

	/**
	 * The collection of active players.
	 */
	private final ActorList<Player> players = new ActorList<>(2048);

	/**
	 * A collection of {@link Player}s registered by their username hashes.
	 */
	public final Long2ObjectMap<Player> playerByNames = new Long2ObjectOpenHashMap<>();

	/**
	 * Amount of staff players online.
	 */
	private int staffCount;
	
	/**
	 * The time the server has been running.
	 */
	private static final Stopwatch RUNNING_TIME = new Stopwatch().reset();
	
	/**
	 * The {@link RegionManager} that manages region caching.
	 */
	private static final RegionManager REGION_MANAGER = new RegionManager();
	
	/**
	 * Client implementation of a smart pathfinder.
	 */
	private static final AStarPathFinder SMART_PATH_FINDER = new AStarPathFinder();
	
	/**
	 * This world's straight line pathfinder used for NPCs movements.
	 */
	private static final SimplePathFinder SIMPLE_PATH_FINDER = new SimplePathFinder();
	
	/**
	 * This world's straight line path checker for combat.
	 */
	private static final SimplePathChecker SIMPLE_PATH_CHECKER = new SimplePathChecker();
	
	/**
	 * This world's {@link LoggingManager} used to log player actions.
	 */
	private static final LoggingManager LOG_MANAGER = new LoggingManager();
	
	/**
	 * A integral {@link Task} that handles ranomized movement of all {@link Mob}s.
	 */
	private static final MobMovementTask NPC_MOVEMENT_TASK = new MobMovementTask();



	private final WorldUtil worldUtil = new WorldUtil(this);





	/* ASSETS GATHERS METHODS. */

	/**
	 * Gets the manager for the queue of game tasks.
	 * @return the queue of tasks.
	 */
	public TaskManager getTask() {
		return taskManager;
	}

	/**
	 * Gets the collection of active players.
	 * @return the active players.
	 */
	public ActorList<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the map of all player by their hashed usernames.
	 * @return player online map.
	 */
	public Long2ObjectMap<Player> getPlayerByNames() {
		return playerByNames;
	}

	/**
	 * Gets the staff count online.
	 * @return staff count.
	 */
	public int getStaffCount() {
		return staffCount;
	}

	/**
	 * Sets a new staff count.
	 * @param staffCount staff count to set.
	 */
	public void setStaffCount(int staffCount) {
		this.staffCount = staffCount;
	}

	/**
	 * Gets the collection of active mobs.
	 * @return the active mobs.
	 */
	public ActorList<Mob> getMobRepository() {
		return mobRepository;
	}

	/**
	 * Returns this world's {@link LoggingManager}.
	 */
	public static LoggingManager getLoggingManager() {
		return LOG_MANAGER;
	}
	
	/**
	 * Returns the running time {@link Stopwatch}.
	 */
	public static Stopwatch getRunningTime() {
		return RUNNING_TIME;
	}
	
	/**
	 * Returns this world's {@link RegionManager}.
	 */
	public static RegionManager getRegions() {
		return REGION_MANAGER;
	}
	
	/**
	 * Client implementation of a smart pathfinder.
	 * @return the pathfinder instance.
	 */
	public static AStarPathFinder getSmartPathfinder() {
		return SMART_PATH_FINDER;
	}
	
	/**
	 * Returns this world's straight pathfinder.
	 */
	public static SimplePathFinder getSimplePathfinder() {
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
	public static MobMovementTask getNpcMovementTask() {
		return NPC_MOVEMENT_TASK;
	}
	
	/**
	 * Returns the singleton pattern implementation.
	 * @return The returned implementation.
	 */
	public static World get() {
		return singleton;
	}

	/**
	 * Returns the singleton pattern {@link Environment}.
	 * @return The returned environment.
	 */
	public Environment getEnvironment() {
		return environment;
	}


	/**
	 * Returns the singleton patter {@link DatabaseTransactionWorker}.
	 * @return The returned worker.
	 */
	public DatabaseTransactionWorker getDatabaseWorker() {
		return databaseWorker;
	}

	/**
	 * Get's the logger used by the world to log various happenings.
	 * @return The logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}



	public Discord getDiscord() {
		return discord;
	}


	public static AttributeMap getAttributeMap() {
		return attributeMap;
	}


	/**
	 * Gets the {@link GameService}.
	 *
	 * @return The GameService.
	 */
	public GameService getGameService() {
		return services.getGame();
	}

	/**
	 * Gets the {@link ServiceManager}.
	 * @return The service manager.
	 */
	public ServiceManager getServices() {
		return services;
	}

	/**
	 * Gets the {@link LoginService}.
	 *
	 * @return The LoginService.
	 */
	public LoginService getLoginService() {
		return services.getLogin();
	}

	public Release getRelease() {
		return release;
	}

	public WorldUtil getWorldUtil() {
		return worldUtil;
	}
}