package net.arrav;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.*;
import net.arrav.cache.FileSystem;
import net.arrav.cache.decoder.MapDefinitionDecoder;
import net.arrav.cache.decoder.ObjectDefinitionDecoder;
import net.arrav.cache.decoder.RegionDecoder;
import net.arrav.content.PlayerPanel;
import net.arrav.content.RestoreSpecialTask;
import net.arrav.content.RestoreStatTask;
import net.arrav.content.commands.CommandDispatcher;
import net.arrav.content.object.pit.FirepitManager;
import net.arrav.content.object.star.ShootingStarManager;
import net.arrav.content.scoreboard.ScoreboardTask;
import net.arrav.content.trivia.TriviaTask;
import net.arrav.net.ArravChannelInitializer;
import net.arrav.net.NetworkConstants;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.task.Task;
import net.arrav.util.LoggerUtils;
import net.arrav.util.Utility;
import net.arrav.util.json.impl.*;
import net.arrav.world.World;
import net.arrav.world.entity.actor.attribute.AttributeKey;
import net.arrav.world.entity.actor.combat.attack.listener.CombatListenerDispatcher;
import net.arrav.world.locale.InstanceManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.netty.util.ResourceLeakDetector.Level.DISABLED;
import static io.netty.util.ResourceLeakDetector.Level.PARANOID;

/**
 * The main class that will register and bind the server effectively.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 */
public final class Arrav {
	
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
	private final static Logger LOGGER = LoggerUtils.getLogger(Arrav.class);
	
	static {
		//System.out.println("Lines in project: " + Utility.linesInProject(new File("./src/")));
		try {
			Thread.currentThread().setName("ArravInitializationThread");
		} catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Invoked when this program is started, initializes the {@link Arrav}.
	 * @param args The runtime arguments, none of which are parsed.
	 */
	public static void main(String[] args) {
		boolean online = Boolean.parseBoolean(args[0]);
		if(online) {
			Runtime.getRuntime().addShutdownHook(new GameShutdownHook());
			DEBUG = false;
		}
		try {
			Arrav arrav = new Arrav();
			arrav.init();
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Error in game run time!", e);
			System.exit(0);
		}
	}
	
	/**
	 * A package-private constructor to discourage external instantiation.
	 */
	public Arrav() {
		ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("ArravInitialization").build());
		launch = MoreExecutors.listeningDecorator(delegateService);
	}
	
	/**
	 * Initializing all of the individual modules.
	 * @throws Exception If any exceptions are thrown during initialization.
	 */
	private void init() {
		try {
			long time = System.currentTimeMillis();
			LOGGER.info("Arrav is being initialized...");
			prepare();
			bind();
			initTasks();
			launch.shutdown();
			launch.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			World.get().startAsync().awaitRunning();
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
			LOGGER.info("Arrav is now online (" + time + ").");
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
	private void bind() {
		LOGGER.info("Binding Arrav on port " + NetworkConstants.PORT_ONLINE + ".");
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup loopGroup;
		//If epoll possible, better to use it (for linux systems).
		if (Epoll.isAvailable()) {
			loopGroup = new EpollEventLoopGroup();
			bootstrap.channel(EpollServerSocketChannel.class);
		} else {
			loopGroup = new NioEventLoopGroup();
		}
		ResourceLeakDetector.setLevel(DEBUG ? PARANOID : DISABLED);
		bootstrap.group(loopGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ArravChannelInitializer());
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