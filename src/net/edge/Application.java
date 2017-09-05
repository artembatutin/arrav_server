package net.edge;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.*;
import net.edge.cache.FileSystem;
import net.edge.cache.decoder.MapDefinitionDecoder;
import net.edge.cache.decoder.ObjectDefinitionDecoder;
import net.edge.cache.decoder.RegionDecoder;
import net.edge.content.PlayerPanel;
import net.edge.content.RestoreSpecialTask;
import net.edge.content.RestoreStatTask;
import net.edge.content.commands.CommandDispatcher;
import net.edge.content.object.pit.FirepitManager;
import net.edge.content.object.star.ShootingStarManager;
import net.edge.content.scoreboard.ScoreboardManager;
import net.edge.content.trivia.TriviaTask;
import net.edge.net.EdgevilleChannelInitializer;
import net.edge.net.NetworkConstants;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;
import net.edge.task.Task;
import net.edge.util.LoggerUtils;
import net.edge.util.Utility;
import net.edge.util.json.impl.*;
import net.edge.world.World;
import net.edge.world.entity.actor.attribute.AttributeKey;
import net.edge.world.entity.actor.combat.attack.listener.CombatListenerDispatcher;
import net.edge.world.locale.InstanceManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The main class that will register and bind the server effectively.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 */
public final class Application {
	
	/**
	 * The flag that determines if debugging messages should be printed or not.
	 */
	public static boolean DEBUG = true;
	
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
	private final static Logger LOGGER = LoggerUtils.getLogger(Application.class);
	
	static {
		//System.out.println("Lines in project: " + Utility.linesInProject(new File("./src/")));
		try {
			Thread.currentThread().setName("EdgevilleInitializationThread");
		} catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Invoked when this program is started, initializes the {@link Application}.
	 * @param args The runtime arguments, none of which are parsed.
	 */
	public static void main(String[] args) {
		boolean online = Boolean.parseBoolean(args[0]);
		if(online) {
			Runtime.getRuntime().addShutdownHook(new GameShutdownHook());
			DEBUG = false;
		}
		try {
			Application edgeville = new Application();
			edgeville.init();
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Error in game run time!", e);
			System.exit(0);
		}
	}
	
	/**
	 * A package-private constructor to discourage external instantiation.
	 */
	public Application() {
		ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("EdgevilleInitialization").build());
		launch = MoreExecutors.listeningDecorator(delegateService);
	}
	
	/**
	 * Initializing all of the individual modules.
	 * @throws Exception If any exceptions are thrown during initialization.
	 */
	public void init() throws Exception {
		try {
			LOGGER.info("Main is being initialized...");
			prepare();
			bind();
			initTasks();
			launch.shutdown();
			launch.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			World.get().start();
			InstanceManager.get().close(0);
			TriviaTask.getBot().submit();
			World.get().submit(World.getNpcMovementTask());
			World.get().submit(new RestoreSpecialTask());
			World.get().submit(new RestoreStatTask());
			World.get().submit(new Task(100, false) {
				@Override
				public void execute() {
					PlayerPanel.UPTIME.refreshAll("@or2@ - Uptime: @yel@" + Utility.timeConvert(World.getRunningTime().elapsedTime(TimeUnit.MINUTES)));
					LocalDate date = LocalDate.now();
					ScoreboardManager score = ScoreboardManager.get();
					if(date.getDayOfWeek().equals(DayOfWeek.MONDAY) && !score.isResetPlayerScoreboardStatistic()) {
						score.resetPlayerScoreboard();
					} else if(!date.getDayOfWeek().equals(DayOfWeek.MONDAY) && score.isResetPlayerScoreboardStatistic()) {
						score.setResetPlayerScoreboardStatistic(false);
					}
					ShootingStarManager.get().process();
				}
			});
			LOGGER.info("Edgeville is now online!");
			STARTING = false;
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "An error occurred while binding the Bootstrap!", e);
			// No point in continuing server startup when the
			// bootstrap either failed to bind or was bound
			// incorrectly.
			System.exit(1);
		}
	}
	
	/**
	 * Initializes the Netty implementation. Will block indefinitely until the {@link ServerBootstrap} is bound.
	 * @throws Exception If any exceptions are thrown while binding.
	 */
	private void bind() throws Exception {
		//		if we run server on a unix machine, then we can use a native implementation instead of nio
		//		if (Epoll.isAvailable()) {
		//			EventLoopGroup loopGroup = new EpollEventLoopGroup();
		//			bootstrap.channel(EpollServerSocketChannel.class);
		//		}
		LOGGER.info("Binding Edgeville on port " + NetworkConstants.PORT_ONLINE + ".");
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		ResourceLeakDetector.setLevel(DEBUG ? ResourceLeakDetector.Level.PARANOID : ResourceLeakDetector.Level.DISABLED);
		bootstrap.group(loopGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new EdgevilleChannelInitializer());
		bootstrap.bind(NetworkConstants.PORT_ONLINE).syncUninterruptibly();
		
	}
	
	/**
	 * Initializes all miscellaneous startup tasks asynchronously.
	 * @throws Exception If any exceptions are thrown while initializing startup tasks.
	 */
	private void initTasks() throws Exception {
		FileSystem fs = FileSystem.create("data/cache");
		AttributeKey.init();
		//object/region decoding must be done before parallel.
		new ObjectDefinitionDecoder(fs).run();
		new MapDefinitionDecoder(fs).run();
//		new RegionDecoder(fs).run();
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
		launch.execute(new AreaLoader());
		launch.execute(new AreaMultiLoader());
		launch.execute(new ShopLoader());
		launch.execute(new ClanChatLoader());
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
		loadEvents();
		ButtonAction.init();
		ItemAction.init();
		ItemOnObjectAction.init();
		ItemOnItemAction.init();
		MobAction.init();
		ObjectAction.init();
	}
	
	public static void loadEvents() {
		for(String directory : Utility.getSubDirectories(ActionInitializer.class)) {
			try {
				List<ActionInitializer> s = Utility.getClassesInDirectory(ActionInitializer.class.getPackage().getName() + "." + directory).stream().map(clazz -> (ActionInitializer) clazz).collect(Collectors.toList());
				s.forEach(ActionInitializer::init);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}