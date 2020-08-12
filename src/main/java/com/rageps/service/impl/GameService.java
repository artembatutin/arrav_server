package com.rageps.service.impl;

import com.rageps.GameConstants;
import com.rageps.GamePulseHandler;
import com.rageps.net.refactor.codec.login.LoginConstants;
import com.rageps.net.refactor.message.MessageHandlerChainSet;
import com.rageps.net.refactor.session.impl.GameSession;
import com.rageps.net.refactor.session.impl.LoginSession;
import com.rageps.service.Service;
import com.rageps.util.ThreadUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.ActorList;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.region.Region;
import com.rageps.world.sync.Synchronizer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rageps.net.Session.UPDATE_LIMIT;

/**
 * The {@link GameService} class schedules and manages the execution of the {@link GamePulseHandler} class.
 *
 * @author Graham
 */
public final class GameService extends Service {

	/**
	 * A utility class wrapping a {@link Player} and their corresponding {@link LoginSession}.
	 */
	private static final class LoginPlayerRequest {

		/**
		 * The Player.
		 */
		private final Player player;

		/**
		 * The LoginSession.
		 */
		private final LoginSession session;

		/**
		 * Creates the LoginPlayerRequest.
		 *
		 * @param player The {@link Player} logging in.
		 * @param session The {@link LoginSession} of the Player.
		 */
		public LoginPlayerRequest(Player player, LoginSession session) {
			this.player = player;
			this.session = session;
		}

	}

	/**
	 * The amount of players to deregister per cycle. This is to ensure the saving threads don't get swamped with
	 * requests and slow everything down.
	 */
	private static final int DEREGISTRATIONS_PER_CYCLE = 50;

	/**
	 * The amount of players to register per cycle.
	 */
	private static final int REGISTRATIONS_PER_CYCLE = 25;

	/**
	 * The World this Service is for.
	 */
	protected final World world;

	/**
	 * The scheduled executor service.
	 */
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(ThreadUtil.create("GameService"));

	/**
	 * The Queue of LoginPlayers to add.
	 */
	private final Queue<LoginPlayerRequest> newPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The Queue of Players to remove.
	 */
	private final Queue<Player> oldPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The {@link MessageHandlerChainSet}.
	 */
	private MessageHandlerChainSet handlers;

	/**
	 * Main game synchronizer.
	 */
	private final Synchronizer sync = new Synchronizer();

	/**
	 * Creates the GameService.
	 *
	 * @param world The {@link World} the GameService is for.
	 * @throws Exception If an error occurs during initialization.
	 */
	public GameService(World world) {
		this.world = world;
		init();
	}

	/**
	 * Finalizes the registration of a player.
	 *
	 * @param player The player.
	 */
	public synchronized void finalizePlayerRegistration(Player player) {
		world.register(player);
		Region region = player.getRegion();//world.getRegionRepository().fromPosition(player.getPosition());
		region.add(player);

		if (!player.getSession().isReconnecting()) {
			//player.sendInitialMessages();
		}
	}

	/**
	 * Finalizes the unregistration of a player.
	 *
	 * @param player The player.
	 */
	public synchronized void finalizePlayerUnregistration(Player player) {
		world.unregister(player);
	}

	/**
	 * Gets the MessageHandlerChainSet
	 *
	 * @return The MessageHandlerChainSet.
	 */
	public MessageHandlerChainSet getMessageHandlerChainSet() {
		return handlers;
	}



	/**
	 * The regional tick counter for processing such as {@link GroundItem} in a region.
	 */
	private int regionalTick;

	/**
	 * The time it took in milliseconds to do the sync.
	 */
	public static long millis;

	/**
	 * Called every pulse.
	 */
	public synchronized void pulse() {
		finalizeRegistrations();
		finalizeUnregistrations();


		final long start = System.currentTimeMillis();
		//int logs = logins.size();
		synchronized(this) {
			//dequeueLogins();
			//registerActors();

			//register

			sync.preUpdate(world.getPlayers(), world.getMobRepository());
			sync.update(world.getPlayers());
			sync.postUpdate(world.getPlayers(), world.getMobRepository());

			//deregister

			//dequeueLogout();
			//disposeActors();
			regionalTick++;
			if(regionalTick == 10) {
				Region.cleanup();
				regionalTick = 0;
			}
		}

		millis = System.currentTimeMillis() - start;
		if(millis > 400) {
			UPDATE_LIMIT -= 20;
			if(UPDATE_LIMIT < 40)
				UPDATE_LIMIT = 40;
		} else if(UPDATE_LIMIT < 200) {
			UPDATE_LIMIT += 20;
		}



		ActorList<Player> players = world.getPlayers();
		for (Player player : players) {
			GameSession session = player.getSession();

			if (session != null) {
				session.handlePendingMessages(handlers);
			}
		}
		world.pulse();
	}

	/**
	 * Registers a {@link Player} at the end of the next cycle.
	 *
	 * @param player The Player to register.
	 * @param session the {@link LoginSession} of the Player.
	 */
	public void registerPlayer(Player player, LoginSession session) {
		newPlayers.add(new LoginPlayerRequest(player, session));
	}

	/**
	 * Shuts down this game service.
	 *
	 * @param natural Whether or not the shutdown was expected.
	 */
	public void shutdown(boolean natural) {
		executor.shutdownNow();
		// TODO: Other events that should happen upon natural or unexpected shutdown.
	}

	@Override
	public void start() {
		executor.scheduleAtFixedRate(new GamePulseHandler(this), GameConstants.PULSE_DELAY, GameConstants.PULSE_DELAY,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * Unregisters a player. Returns immediately. The player is unregistered at the start of the next cycle.
	 *
	 * @param player The player.
	 */
	public void unregisterPlayer(Player player) {
		oldPlayers.add(player);
	}

	/**
	 * Finalizes the registration of Player's queued to be registered.
	 */
	private void finalizeRegistrations() {
		for (int count = 0; count < REGISTRATIONS_PER_CYCLE; count++) {
			LoginPlayerRequest request = newPlayers.poll();
			if (request == null) {
				break;
			}

			Player player = request.player;
			if (world.isPlayerOnline(player.getUsername())) {
				request.session.sendLoginFailure(LoginConstants.STATUS_ACCOUNT_ONLINE);
			} else if (world.getPlayers().isFull()) {
				request.session.sendLoginFailure(LoginConstants.STATUS_SERVER_FULL);
			} else {
				request.session.sendLoginSuccess(player);
				finalizePlayerRegistration(player);
			}
		}
	}

	/**
	 * Finalizes the unregistration of Player's queued to be unregistered.
	 */
	private void finalizeUnregistrations() {
		LoginService loginService = world.getLoginService();

		for (int count = 0; count < DEREGISTRATIONS_PER_CYCLE; count++) {
			Player player = oldPlayers.poll();
			if (player == null) {
				break;
			}

			loginService.submitSaveRequest(player.getSession(), player);
		}
	}

	/**
	 * Initializes the game service.
	 */
	private void init() {
		/*try (InputStream input = new FileInputStream("data/messages.xml")) {
			MessageHandlerChainSetParser chainSetParser = new MessageHandlerChainSetParser(input);
			handlers = chainSetParser.parse(world);
		}

		try (InputStream input = new FileInputStream("data/synchronizer.xml")) {
			XmlParser parser = new XmlParser();
			XmlNode root = parser.parse(input);

			if (!root.getName().equals("synchronizer")) {
				throw new IOException("Invalid root node name.");
			}

			XmlNode active = root.getChild("active");
			if (active == null || !active.hasValue()) {
				throw new IOException("No active node/value.");
			}

			Class<?> clazz = Class.forName(active.getValue());
			synchronizer = (ClientSynchronizer) clazz.newInstance();
		}*/
	}

}
