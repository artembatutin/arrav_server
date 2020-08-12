package com.rageps.world;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.rageps.net.discord.Discord;
import com.rageps.net.packet.out.SendBroadcast;
import com.rageps.net.refactor.release.Release;
import com.rageps.net.refactor.release.Release317;
import com.rageps.net.sql.DatabaseTransactionWorker;
import com.rageps.service.ServiceManager;
import com.rageps.service.impl.GameService;
import com.rageps.service.impl.LoginService;
import com.rageps.world.attr.AttributeMap;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;
import com.rageps.world.env.Environment;
import com.rageps.world.env.JsonEnvironmentProvider;
import com.rageps.world.text.ColorConstants;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.rageps.GameConstants;
import com.rageps.content.PlayerPanel;
import com.rageps.command.impl.UpdateCommand;
import com.rageps.net.packet.out.SendLogout;
import com.rageps.net.packet.out.SendYell;
import com.rageps.task.Task;
import com.rageps.task.TaskManager;
import com.rageps.util.Stopwatch;
import com.rageps.util.TextUtils;
import com.rageps.util.log.LoggingManager;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.ActorList;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobMovementTask;
import com.rageps.world.entity.actor.move.path.AStarPathFinder;
import com.rageps.world.entity.actor.move.path.SimplePathChecker;
import com.rageps.world.entity.actor.move.path.impl.SimplePathFinder;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.region.Region;
import com.rageps.world.entity.region.RegionManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.rageps.world.entity.EntityState.AWAITING_REMOVAL;

/**
 * The static utility class that contains functions to manage and process game characters.
 * @author Artem Batutin
 */
public final class World extends AbstractScheduledService {


	/**
	 * The LOGGER that will print important information.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * An implementation of the singleton pattern to prevent indirect
	 * instantiation of this class file.
	 */
	private static final World singleton = new World();

	private static final Release RELEASE = new Release317();

	/**
	 * An environment provided with configuration related to the specific
	 * environment the server is running on.
	 */
	private static final Environment ENVIRONMENT = JsonEnvironmentProvider.provide();

	/**
	 * This Discord for this World
	 */
	private static final Discord discord = new Discord();

	/**
	 * The service manager.
	 */
	private final ServiceManager services = new ServiceManager(this);

	/**
	 * Responsible for asynchronously executing all database transactions.
	 */
	private static final DatabaseTransactionWorker DATABASE_WORKER = new DatabaseTransactionWorker();

	/**
	 * Responsible for all saving/loading of character files using it's delegated method.
	 */
	private static final PlayerPersistenceManager PERSISTENCE_MANAGER = new PlayerPersistenceManager();

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
	//private final Queue<Player> logins = new ConcurrentLinkedQueue<>();
	
	/**
	 * The queue of {@link Player}s waiting to be logged out.
	 */
	//private final Queue<Player> logouts = new ConcurrentLinkedQueue<>();
	
	/**
	 * The queue of {@link Actor}s waiting to be added to the world.
	 */
	//private final Queue<Actor> registeringActors = new ConcurrentLinkedQueue<>();
	
	/**
	 * The queue of {@link Actor}s waiting to be removed from the world.
	 */
	//private final Queue<Actor> disposingActors = new ConcurrentLinkedQueue<>();


	/**
	 * The Queue of Npcs that have yet to be added to the repository.
	 */
	private final Queue<Mob> queuedNpcs = new ArrayDeque<>();

	/**
	 * The Queue of Npcs that have yet to be removed from the repository.
	 */
	private final Queue<Mob> oldNpcs = new ArrayDeque<>();


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
	private final Long2ObjectMap<Player> playerByNames = new Long2ObjectOpenHashMap<>();
	
	/**
	 * Amount of staff players online.
	 */
	private int staffCount;
	

	
	@Override
	protected String serviceName() {
		return "GamePulse";
	}
	
	@Override
	protected void runOneIteration() {

		//System.out.println("took: " + millis + " - players: " + players.size() + " - mobs: " + mobs.size() + " - logins: " + logs);
	}
	
	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedDelaySchedule(600, 600, TimeUnit.MILLISECONDS);
	}

	/**
	 * Pulses this World.
	 */
	public void pulse() {
		unregisterNpcs();
		registerNpcs();
		taskManager.sequence();
	}


	/**
	 * Queues {@code player} to be logged in on the next server sequence.
	 * @param player the player to log in.
	 */
	/*public void queueLogin(Player player) {
		if(player.getState() == IDLE) {
			logins.offer(player);
		}
	}*/
	
	/**
	 * Dequeue all incoming connecting players.
	 */
	/*private void dequeueLogins() {
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
	 */
	private void registerActors() {
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
	 * Registers all of the {@link Mob}s in the {@link #queuedNpcs queue}.
	 */
	private void registerNpcs() {
		while (!queuedNpcs.isEmpty()) {
			Mob npc = queuedNpcs.poll();
			boolean success = mobRepository.add(npc);

			if (success) {
				Region region = npc.getRegion();//regions.fromPosition(npc.getPosition());
				region.add(npc);

				//if (npc.hasBoundaries()) {
				//	npcMovement.addNpc(npc);
				//}
			} else {
				getLogger().warn("Failed to register npc (capacity reached): [count={}]",mobRepository.size());
			}
		}
	}

	/**
	 * Unregisters all of the {@link Mob}s in the {@link #oldNpcs queue}.
	 */
	private void unregisterNpcs() {
		while (!oldNpcs.isEmpty()) {
			Mob npc = oldNpcs.poll();

			Region region = npc.getRegion();//regions.fromPosition(npc.getPosition());
			region.remove(npc);

			mobRepository.remove(npc);
		}
	}

	/**
	 * Registers the specified {@link Mob}.
	 *
	 * @param npc The Npc.
	 */
	public void register(Mob npc) {
		queuedNpcs.add(npc);
	}

	/**
	 * Registers the specified {@link Player}.
	 *
	 * @param player The Player.
	 */
	public void register(Player player) {
		String username = player.getUsername();

		playerRepository.add(player);
		players.put(NameUtil.encodeBase37(username), player);

		logger.finest("Registered player: " + player + " [count=" + playerRepository.size() + "]");
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
	/*private void disposeActors() {
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
//	}*/

	/**
	 * Queues {@code player} to be logged out on the next server sequence.
	 * @param player the player to log out.
	 */
	public void queueLogout(Player player) {
		if(player.getCombat().inCombat())
			player.getLogoutTimer().reset();
		player.setState(AWAITING_REMOVAL);
		player.out(new SendLogout());
		logouts.add(player);
	}
	
	/**
	 * Dequeue the logged out demands.
	 */
	private void dequeueLogout() {
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
	 * Returns a player within an optional whose name hash is equal to
	 * {@code username}.
	 * @param username the name hash to check the collection of players for.
	 * @return the player within an optional if found, or an empty optional if
	 * not found.
	 */
	public Optional<Player> getPlayer(long username) {
		return Optional.ofNullable(playerByNames.get(username));
	}

	/**
	 * Checks if one player is the same person or is logged in from the same machine/host as
	 * the other player.
	 * @param p1 The first player.
	 * @param p2 The second player.
	 * @return Whether or not they are connected physically.
	 */
	public boolean samePerson(Player p1, Player p2) {
		return p1.getSession().getHost().equals(p2.getSession().getHost()) ||
				p1.getSession().getUid().equals(p2.getSession().getUid()) ||
				p1.getSession().getMacAddress().equals(p2.getSession().getUid());
	}

	/**
	 * Returns a player within an optional whose name is equal to
	 * {@code username}.
	 * @param username the name to check the collection of players for.
	 * @return the player within an optional if found, or an empty optional if
	 * not found.
	 */
	public Optional<Player> getPlayer(String username) {
		return getPlayer(TextUtils.nameToHash(username));
	}
	
	/**
	 * Retrieves and returns the local {@link Player}s for {@code character}.
	 * The specific players returned is completely dependent on the character
	 * given in the argument.
	 * @param character the character that it will be returned for.
	 * @return the local players.
	 */
	public Iterator<Player> getLocalPlayers(Actor character) {
		if(character.isPlayer())
			return character.toPlayer().getLocalPlayers().iterator();
		return players.iterator();
	}
	
	/**
	 * Retrieves and returns the local {@link Mob}s for {@code character}. The
	 * specific mobs returned is completely dependent on the character given in
	 * the argument.
	 * @param character the character that it will be returned for.
	 * @return the local mobs.
	 */
	public Iterator<Mob> getLocalMobs(Actor character) {
		if(character.isPlayer())
			return character.toPlayer().getLocalMobs().iterator();
		return mobRepository.iterator();
	}
	
	/**
	 * Creates a set of every single actor in the player and npc character
	 * lists, with {@link Mob} actors first and {@link Player} actors second.
	 * @return a set containing every single character.
	 */
	public Set<Actor> getActors() {
		Set<Actor> actors = new HashSet<>();
		mobRepository.forEach(actors::add);
		players.forEach(actors::add);
		return actors;
	}

	/**
	 * Sends a broadcast to the entire world.
	 * @param time
	 * @param message
	 * @param countdown
	 */
	public void sendBroadcast(int time, String message, boolean countdown) {
		get().players.stream().forEach($it -> {
			$it.out(new SendBroadcast(countdown ? 0 : 1, time, (message)));
			$it.message("<img=29>["+ ColorConstants.MAGENTA +"RagePS</col>]" + (message));
		});
	}
	
	/**
	 * Sends {@code message} to all online players with an announcement dependent of {@code announcement}.
	 * @param message the message to send to all online players.
	 * @param announcement determines if this message is an announcement.
	 */
	public void message(String message, boolean announcement) {
		Player p;
		Iterator<Player> it = players.iterator();
		while((p = it.next()) != null) {
			p.message((announcement ? "@red@[ANNOUNCEMENT]: " : "") + message);
		}
	}
	
	/**
	 * Sends {@code message} to all online players.
	 * @param message the message to send to all online players.
	 */
	public void message(String message) {
		message(message, false);
	}
	
	/**
	 * Sends {@code message} to all online players as a yell.
	 * @param author author yelling.
	 * @param message the message being yelled.
	 * @param rights the rights of the author.
	 */
	public void yell(String author, String message, Rights rights) {
		Player p;
		Iterator<Player> it = players.iterator();
		while((p = it.next()) != null) {
			p.out(new SendYell(author, message, rights));
		}
	}

	public void messageIf(String message, Predicate<? super Player> filter) {
		players.stream().filter(Objects::nonNull).filter(filter).forEach(p -> p.message(message));
	}
	public void broadcastIf(Predicate<? super Player> filter, int time, String message, boolean countdown) {
		players.stream().filter(Objects::nonNull).filter(filter).forEach(p -> p.out(new SendBroadcast(countdown ? 0 : 1, time, (message))));
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

	/**
	 * Get's a players alt accounts.
	 * Todo - move to some other world util class
	 */
	public ObjectArrayList<Player> getAlts(Player player) {
		ObjectArrayList<Player> alts = new ObjectArrayList<>();

		for(Player p : getPlayers()) {
			if(samePerson(player, p))
				alts.add(p);
		}
		return alts;
	}

	/** Gets a player by name.
	 * Todo - move to some other world util class
	 * */
	public Optional<Player> search(String name) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}

			if (player.credentials.username.equalsIgnoreCase(name)) {
				return Optional.of(player);
			}
		}
		return Optional.empty();
	}
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
	
	
	/* CONSTANTS DECLARATIONS */
	
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

	/* ASSETS GATHERS METHODS. */
	
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
		return ENVIRONMENT;
	}


	/**
	 * Returns the singleton patter {@link DatabaseTransactionWorker}.
	 * @return The returned worker.
	 */
	public DatabaseTransactionWorker getDatabaseWorker() {
		return DATABASE_WORKER;
	}

	public PlayerPersistenceManager getPersistenceManager() {
		return PERSISTENCE_MANAGER;
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
	 * Gets the {@link LoginService}.
	 *
	 * @return The LoginService.
	 */
	public LoginService getLoginService() {
		return services.getLogin();
	}

	public Release getRelease() {
		return RELEASE;
	}
}