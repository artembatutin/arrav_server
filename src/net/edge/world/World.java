package net.edge.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.edge.Application;
import net.edge.GameConstants;
import net.edge.GamePulseHandler;
import net.edge.content.combat.Combat;
import net.edge.content.commands.impl.UpdateCommand;
import net.edge.net.database.Database;
import net.edge.net.database.pool.ConnectionPool;
import net.edge.net.packet.out.SendLogout;
import net.edge.net.packet.out.SendYell;
import net.edge.task.Task;
import net.edge.task.TaskManager;
import net.edge.util.Stopwatch;
import net.edge.util.TextUtils;
import net.edge.util.ThreadUtil;
import net.edge.util.log.LoggingManager;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.ActorList;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobAggression;
import net.edge.world.entity.actor.mob.MobMovementTask;
import net.edge.world.entity.actor.move.path.SimplePathChecker;
import net.edge.world.entity.actor.move.path.impl.SimplePathFinder;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.region.Region;
import net.edge.world.entity.region.RegionManager;
import net.edge.world.sync.Synchronizer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.edge.net.session.GameSession.outLimit;
import static net.edge.world.entity.EntityState.AWAITING_REMOVAL;
import static net.edge.world.entity.EntityState.IDLE;

/**
 * The static utility class that contains functions to manage and process game characters.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class World {
	
	/**
	 * An implementation of the singleton pattern to prevent indirect
	 * instantiation of this class file.
	 */
	private static final World singleton = new World();
	
	/**
	 * Main game synchronizer.
	 */
	private final Synchronizer sync = new Synchronizer();
	
	/**
	 * The scheduled executor service.
	 */
	private final ScheduledExecutorService pulser = Executors.newSingleThreadScheduledExecutor(ThreadUtil.create("GamePulse"));
	
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
	private final ActorList<Mob> mobs = new ActorList<>(16384);
	
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
	
	/**
	 * The regional tick counter for processing such as {@link GroundItem} in a region.
	 */
	private int regionalTick;
	
	/**
	 * The time it took in milliseconds to do the sync.
	 */
	public static long millis;
	
	static {
		int amtCpu = Runtime.getRuntime().availableProcessors();
		try {
			score = new Database(Application.DEBUG ? "192.99.101.90" : "127.0.0.1", "edge_score", Application.DEBUG ? "edge_avro" : "root", Application.DEBUG ? "%GL5{)hAJBU(MB3h" : "rooty412JlW", amtCpu);
		} catch(Exception e) {e.printStackTrace();}
		try {
			donation = new Database(Application.DEBUG ? "192.99.101.90" : "127.0.0.1", "edge_donate", Application.DEBUG ? "edge_avro" : "root", Application.DEBUG ? "%GL5{)hAJBU(MB3h" : "rooty412JlW", amtCpu);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Starts the synchronized pulser.
	 */
	public void start() {
		pulser.scheduleAtFixedRate(new GamePulseHandler(this), 600, 600, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Sends a task to the synchronized pulser.
	 * @param r task to be sent.
	 */
	public void run(Runnable r) {
		pulser.submit(r);
	}
	
	/**
	 * Closes the synchronized pulser.
	 */
	public void shutdown() {
		pulser.shutdownNow();
	}
	
	/**
	 * The method that executes the update sequence for all in game characters every cycle.
	 */
	public void pulse() throws InterruptedException {
		final long start = System.currentTimeMillis();
		
		synchronized(this) {
			dequeueLogins();
			registerActors();
			sync.preUpdate(players, mobs);
			taskManager.sequence();
			sync.update(players);
			sync.postUpdate(players, mobs);
			dequeueLogout();
			disposeActors();
			regionalTick++;
			if(regionalTick == 10) {
				Region.cleanup();
				regionalTick = 0;
			}
		}
		
		millis = System.currentTimeMillis() - start;
		if(millis > 600)
			over++;
		if(millis > 400) {
			outLimit -= 20;
			if(outLimit < 40)
				outLimit = 40;
		} else if(outLimit < 200) {
			outLimit += 20;
		}
		//System.out.println("took: " + millis + " - players online: " + players.size() + " parsing packets: " + outLimit + " - went over 600ms " + over + " times");
	}
	
	private int over = 0;
	
	/**
	 * Queues {@code player} to be logged in on the next server sequence.
	 * @param player the player to log in.
	 */
	public void queueLogin(Player player) {
		if (player.getState() == IDLE) {
			logins.offer(player);
		}
	}
	
	/**
	 * Dequeue all incoming connecting players.
	 */
	private void dequeueLogins() {
		if(!logins.isEmpty()) {
			for(int i = 0; i < GameConstants.LOGIN_THRESHOLD; i++) {
				Player player = logins.poll();
				if(player == null) {
					break;
				}
				if(playerByNames.containsKey(player.getCredentials().getUsernameHash())) {
					player.getSession().getChannel().close();
				} else if(players.add(player)) {
					playerByNames.put(player.getCredentials().getUsernameHash(), player);
				} else if(player.isHuman()) {
					player.getSession().getChannel().close();
				}
			}
		}
	}
	
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
				actor.getRegion().ifPresent(r -> r.add(actor));
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
	private void disposeActors() {
		if(!disposingActors.isEmpty()) {
			for(int i = 0; i < disposingActors.size(); i++) {
				Actor actor = disposingActors.poll();
				if(actor == null) {
					break;
				}
				actor.getRegion().ifPresent(r -> r.remove(actor));
			}
		}
	}
	
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
		if (!logouts.isEmpty()) {
			for (int i = 0; i < GameConstants.LOGOUT_THRESHOLD; i++) {
				Player player = logouts.poll();
				if(player == null) {
					continue;
				}
				if (!logout(player)) {
					player.getSession().setActive(false);
					logouts.offer(player);
				}
			}
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
		return mobs.iterator();
	}

	/**
	 * Creates a set of every single actor in the player and npc character
	 * lists, with {@link Mob} actors first and {@link Player} actors second.
	 *
	 * @return a set containing every single character.
	 */
	public Set<Actor> getActors() {
		Set<Actor> actors = new HashSet<>();
		mobs.forEach(actors::add);
		players.forEach(actors::add);
		return actors;
	}
	
	/**
	 * Sends {@code message} to all online players with an announcement dependent of {@code announcement}.
	 * @param message      the message to send to all online players.
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
	 * @param author  author yelling.
	 * @param message the message being yelled.
	 * @param rights  the rights of the author.
	 */
	public void yell(String author, String message, Rights rights) {
		Player p;
		Iterator<Player> it = players.iterator();
		while((p = it.next()) != null) {
			p.out(new SendYell(author, message, rights));
		}
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
				mobs.remove(mob);
			}
			player.getMobs().clear();
			if(response) {
				playerByNames.remove(player.getCredentials().getUsernameHash());
			}
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
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
	public ActorList<Mob> getMobs() {
		return mobs;
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
	public static MobMovementTask getNpcMovementTask() {
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
	public static ConnectionPool getDonation() {
		if(donation == null)
			return null;
		return donation.getPool();
	}
	
	/**
	 * Returns the singleton pattern implementation.
	 * @return The returned implementation.
	 */
	public static World get() {
		return singleton;
	}
	
}