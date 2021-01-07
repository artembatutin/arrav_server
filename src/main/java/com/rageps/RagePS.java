package com.rageps;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rageps.action.impl.*;
import com.rageps.combat.strategy.MobCombatStrategyManager;
import com.rageps.combat.strategy.PlayerWeaponStrategyManager;
import com.rageps.content.clanchannel.ClanRepository;
import com.rageps.content.event.GameEventManager;
import com.rageps.net.refactor.NetworkConstants;
import com.rageps.net.refactor.ServiceChannelInitializer;
import com.rageps.net.refactor.session.impl.ApolloHandler;
import com.rageps.net.sql.clan.ClanLoaderTransaction;
import com.rageps.service.ServiceManager;
import com.rageps.util.Utility;
import com.rageps.util.json.impl.*;
import com.rageps.world.World;
import com.rageps.world.attr.Attributes;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import com.rageps.action.ActionInitializer;
import com.rageps.cache.FileSystem;
import com.rageps.cache.decoder.MapDefinitionDecoder;
import com.rageps.cache.decoder.ObjectDefinitionDecoder;
import com.rageps.cache.decoder.RegionDecoder;
import com.rageps.content.PlayerPanel;
import com.rageps.content.RestoreSpecialTask;
import com.rageps.content.RestoreStatTask;
import com.rageps.command.CommandDispatcher;
import com.rageps.content.itemBoxes.ItemBoxHandler;
import com.rageps.content.object.pit.FirepitManager;
import com.rageps.content.object.star.ShootingStarManager;
import com.rageps.content.scoreboard.ScoreboardTask;
import com.rageps.content.trivia.TriviaTask;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.task.Task;
import com.rageps.combat.listener.CombatListenerDispatcher;
import com.rageps.world.locale.InstanceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.netty.util.ResourceLeakDetector.Level.DISABLED;
import static io.netty.util.ResourceLeakDetector.Level.PARANOID;

/**
 * The main class that will register and bind the server effectively.
 * @author Artem Batutin
 * @author lare96 <http://github.com/lare96>
 */
public final class RagePS {
	
	/**
	 * The flag that determines if the server is starting up.
	 */
	public static boolean STARTING = true;
	
	/**
	 * The status that determines if the server is being updated.
	 */
	public static double UPDATING = 0;
	
	/**
	 * The {@link ExecutorService} that will execute startup tasks.
	 */
	private final ListeningExecutorService launch;
	
	/**
	 * The LOGGER that will print important information.
	 */
	private static final Logger LOGGER = LogManager.getLogger();


	static {
		//System.out.println("Lines in project: " + Utility.linesInProject(new File("./src/")));
		try {
			Thread.currentThread().setName("RageInitializationThread");
		} catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Invoked when this program is started, initializes the {@link RagePS}.
	 * @param args The runtime arguments, none of which are parsed.
	 */
	public static void main(String[] args) {
		/*boolean online = Boolean.parseBoolean(args[0]);
		if(online) {
			Runtime.getRuntime().addShutdownHook(new GameShutdownHook());
			DEBUG = false;
		}*/
		try {
			RagePS ragePS = new RagePS();
			ragePS.init();
		} catch(Exception e) {
			LOGGER.fatal("Error un game run time!", e);
			System.exit(0);
		}
	}
	
	/**
	 * A package-private constructor to discourage external instantiation.
	 */
	public RagePS() {
		ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("RageInitialization").build());
		launch = MoreExecutors.listeningDecorator(delegateService);
	}
	
	/**
	 * Initializing all of the individual modules.
	 * @throws Exception If any exceptions are thrown during initialization.
	 */
	private void init() {
		try {
			long time = System.currentTimeMillis();
			LOGGER.info("RagePS is being initialized...");
			prepare();
			bind();
			initTasks();
			launch.shutdown();
			launch.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			World.get().getServices().startAll();
			InstanceManager.get().close(0);
			TriviaTask.getBot().submit();
			World.get().submit(World.getNpcMovementTask());
			World.get().submit(new RestoreSpecialTask());
			World.get().submit(new RestoreStatTask());
			World.get().submit(new ScoreboardTask());
			World.get().submit(new Task(100, false) {
				@Override
				public void execute() {
					PlayerPanel.UPTIME.refreshAll("@or2@ - Uptime: @yel@" + Utility.timeConvert(World.getRunningTime().elapsedTime(TimeUnit.MINUTES)));
					ShootingStarManager.get().process();
				}
			});
			time = System.currentTimeMillis() - time;
			System.gc();//cleaning up startup.
			LOGGER.info(World.get().getEnvironment().getName()+" is now online (" + time + ").");
			STARTING = false;
		} catch(Exception e) {
			LOGGER.fatal("An error occurred while binding the Bootstrap!", e);
			// No point in continuing server startup when the
			// bootstrap either failed to bind or was bound
			// incorrectly.
			System.exit(1);
		}
	}

	/**
	 * The event loop group.
	 */
	private final EventLoopGroup loopGroup = new NioEventLoopGroup();

	/**
	 * The {@link ServerBootstrap} for the service listener.
	 */
	private final ServerBootstrap serviceBootstrap = new ServerBootstrap();
	
	/**
	 * Initializes the Netty implementation. Will block indefinitely until the {@link ServerBootstrap} is bound.
	 * @throws Exception If any exceptions are thrown while binding.
	 */
	private void bind() {

		ApolloHandler handler = new ApolloHandler();



		serviceBootstrap.group(loopGroup);
		ChannelInitializer<SocketChannel> service = new ServiceChannelInitializer(handler);
		serviceBootstrap.channel(NioServerSocketChannel.class);
		serviceBootstrap.childHandler(service);

		LOGGER.info("Binding RagePs on port " + World.get().getEnvironment().getPort() + ".");

		SocketAddress address = new InetSocketAddress(World.get().getEnvironment().getPort());

		serviceBootstrap.bind(address);


		ResourceLeakDetector.setLevel(World.get().getEnvironment().isDebug() ? PARANOID : DISABLED);
	}
	
	/**
	 * Initializes all miscellaneous startup tasks asynchronously.
	 * @throws Exception If any exceptions are thrown while initializing startup tasks.
	 */
	private void initTasks() throws Exception {
		FileSystem fs = FileSystem.create("data/cache");
		Attributes.init();
		//object/region decoding must be done sequentially.
		new ObjectDefinitionDecoder(fs).run();
		new MapDefinitionDecoder(fs).run();
		new RegionDecoder(fs).run();
		FirepitManager.get().register();
		//Item decoding.
		launch.execute(() -> {
			new ItemDefinitionLoader().load();
			new ItemNodeLoader().load();
			new MarketValueLoader().load();
		});
		//NPC decoding.
		launch.execute(() -> {
			new MobDefinitionLoader().load();
			new MobNodeLoader().load();
			new ItemCacheLoader().load();
			new MobDropTableLoader().load();
			//MobDefinition.dump();
		});
		launch.execute(new AreaMultiLoader());
		launch.execute(new ShopLoader());
		launch.execute(ClanRepository::loadChannels);
		launch.execute(new WeaponPoisonLoader());
		launch.execute(new PacketOpcodeLoader());
		launch.execute(new PacketSizeLoader());
		launch.execute(new SlayerLocationLoader());
		launch.execute(new ShieldAnimationLoader());
		launch.execute(new WeaponAnimationLoader());
		launch.execute(new WeaponInterfaceLoader());
		launch.execute(new EquipmentRequirementLoader());
		launch.execute(new CombatRangedBowLoader());
		launch.execute(new IndividualScoreboardRewardsLoader());
		launch.execute(() -> new SlayerDefinitionLoader().load());
		launch.execute(() -> HostManager.deserialize(HostListType.BANNED_MAC));
		launch.execute(() -> HostManager.deserialize(HostListType.BANNED_IP));
		launch.execute(() -> HostManager.deserialize(HostListType.MUTED_IP));
		launch.execute(() -> HostManager.deserialize(HostListType.STARTER_RECEIVED));
	}
	
	private void prepare() {
		new CombatProjectileLoader().load();
		CombatListenerDispatcher.load();
		CommandDispatcher.load();
		MobCombatStrategyManager.init();
		PlayerWeaponStrategyManager.init();
		loadEvents();
		ButtonAction.init();
		ItemAction.init();
		ItemOnObjectAction.init();
		ItemOnItemAction.init();
		MobAction.init();
		ObjectAction.init();
		ItemBoxHandler.init();
		GameEventManager.loadEvents();
	}

	public static void loadEvents() {
		Set<Class<? extends ActionInitializer>> clazzSet = new Reflections("com.rageps").getSubTypesOf(ActionInitializer.class);
		int i = 0;
		for(Class<?> c : clazzSet) {
			try {
				Object clazz = c.newInstance();
				((ActionInitializer) clazz).init();
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("Successfully loaded " + i + " action listeners." );
	}

}