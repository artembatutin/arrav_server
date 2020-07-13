package com.rageps.net;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.rageps.content.punishment.PunishmentHandler;
import com.rageps.net.codec.crypto.IsaacRandom;
import com.rageps.net.codec.crypto.bcrypt.BCrypt;
import com.rageps.net.codec.crypto.bcrypt.IpsBCrypt;
import com.rageps.net.codec.game.GameDecoder;
import com.rageps.net.codec.game.GameEncoder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.codec.login.LoginRequest;
import com.rageps.net.codec.login.LoginResponse;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.net.rest.RestfulNexus;
import com.rageps.net.rest.account.ForumAccount;
import com.rageps.net.rest.account.ForumCredentials;
import com.rageps.net.rest.account.MultifactorAuthentication;
import com.rageps.net.sql.daily_statistics.DailyStatisticInsertTransaction;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.PlayerEmail;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;
import com.rageps.world.env.Environment;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import static io.netty.util.ReferenceCountUtil.release;
import static io.netty.util.ReferenceCountUtil.releaseLater;
import com.rageps.net.packet.out.SendLogout;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Player's session which handles I/O operations.
 * @author Artem Batutin
 */
public class Session {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * The cap limit of outgoing packets per session.
	 */
	public static int UPDATE_LIMIT = 200;
	
	/**
	 * The ip address that the connection was received from.
	 */
	private final String hostAddress;

	/**
	 * The unique identification used to identify a player.
	 */
	private String uid;

	/**
	 * A unique identification associated with this {@link Session}.
	 */
	private long sessionId;

	/**
	 * The mac address the connection was received from.
	 */
	private String macAddress;
	
	/**
	 * The {@link Channel} to send and receive messages through.
	 */
	private final Channel channel;

	/**
	 * A stopwatch to measure the length of the session.
	 */
	private final Stopwatch sessionStart = Stopwatch.createStarted();

	/**
	 * The player associated with this session.
	 */
	private Player player;
	
	/**
	 * The queue of {@link ByteBuf}s.
	 */
	private BlockingQueue<GamePacket> incoming = new ArrayBlockingQueue<>(NetworkConstants.MESSAGES_PER_TICK);
	
	/**
	 * The queue of {@link ByteBuf}s.
	 */
	private Queue<OutgoingPacket> outgoing = new ConcurrentLinkedQueue<>();
	
	/**
	 * A {@link IsaacRandom}
	 */
	private IsaacRandom encryptor;
	
	/**
	 * Creates a new {@link Session}.
	 * @param channel The {@link Channel} to send and receive messages through.
	 */
	Session(Channel channel) {
		this.channel = channel;
		this.hostAddress = channel == null ? "" : ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
	}
	
	/**
	 * Handles an incoming message from the channel.
	 * @param msg message incoming.
	 * @throws Exception exception
	 */
	void handleMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof GamePacket) {
			GamePacket packet = (GamePacket) msg;
			if(incoming.size() < NetworkConstants.MESSAGES_PER_TICK) {
				incoming.add(packet);
			}
		}
		if(msg instanceof LoginRequest) {
			handleLoginRequest(ctx, (LoginRequest) msg);
		}
	}
	
	/**
	 * Disposes of this {@code Session} by closing the {@link Channel}.
	 */
	void terminate() {
		if(player != null) {
			if(player.getState() != EntityState.AWAITING_REMOVAL && player.getState() != EntityState.INACTIVE) {
				World.get().queueLogout(player);
			} else if(player.getState() == EntityState.ACTIVE) {
				World.get().queueLogout(player);
			}
		}
	}
	
	/**
	 * Unregisters this session.
	 */
	void unregister() {
		terminate();//in case player didn't logged out.
		if(incoming != null) {
			for(GamePacket b : incoming) {
				b.getPayload().release();
			}
			incoming.clear();
		}
		if(player != null) {
			LOGGER.info("Unregistered session for " + player.getFormatUsername());
		}
		player = null;
	}
	
	/**
	 * Handling an incoming packet/message.
	 * @param packet incoming message.
	 */
	public void handle(GamePacket packet) {
		try {
			NetworkConstants.MESSAGES[packet.getOpcode()].handle(player, packet.getOpcode(), packet.getPayload().capacity(), packet);
		} finally {
			packet.getPayload().release();
		}
	}
	
	/**
	 * Polling all incoming packets.
	 */
	public void pollIncomingMessages() {
		while(!incoming.isEmpty()) {
			GamePacket msg = incoming.poll();
			handle(msg);
		}
	}
	
	/**
	 * Writes the given {@link OutgoingPacket} to the stream.
	 */
	public void writeUpdate(OutgoingPacket playerUpdate, OutgoingPacket mobUpdate) {
		if(channel.isActive() && channel.isOpen()) {
			channel.write(playerUpdate.write(player, releaseLater(channel.alloc().buffer(playerUpdate.size()))));
			channel.write(mobUpdate.write(player, releaseLater(channel.alloc().buffer(mobUpdate.size()))));
			while(!outgoing.isEmpty()) {
				OutgoingPacket packet = outgoing.poll();
				ChannelFuture future = channel.write(packet.write(player, releaseLater(channel.alloc().buffer(packet.size()))));
				if(packet.getClass() == SendLogout.class) {
					future.addListener(ChannelFutureListener.CLOSE);
				}
			}
			channel.flush();
		}
	}
	
	public void write(OutgoingPacket packet) {
		if(channel.isActive() && channel.isOpen()) {
			channel.write(packet.write(player, releaseLater(channel.alloc().buffer(packet.size()))));
		}
	}
	
	public void queue(OutgoingPacket packet) {
		//if(channel.isActive() && channel.isOpen()) {
		//	channel.writeAndFlush(packet.write(player), channel.voidPromise());
		//}
		outgoing.add(packet);
	}
	
	/**
	 * Handles a login request.
	 */
	private LoginCode handleRequest(LoginRequest request) {
		this.macAddress = request.getMacAddress();
		this.encryptor = request.getEncryptor();
		player = new Player(new PlayerCredentials(request.getUsername(), request.getPassword()));
		player.setSession(this);
		LoginCode code = LoginCode.NORMAL;

		// Validate the username and password, change login code if needed
		// for invalid credentials or the world being full.
		boolean invalidCredentials = !request.getUsername().matches("^[a-zA-Z0-9_ ]{1,12}$") || request.getPassword().isEmpty() || request.getPassword().length() > 20;
		code = invalidCredentials ? LoginCode.INVALID_CREDENTIALS : World.get().getPlayers().remaining() < 1 ? LoginCode.WORLD_FULL : code;

		// Validating player login possibility.
		if(code == LoginCode.NORMAL && World.get().getPlayer(request.getUsernameHash()).isPresent()) {
			code = LoginCode.ACCOUNT_ONLINE;
		}

		if (World.get().getEnvironment().isSqlEnabled()) {
			try {
				ForumAccount account = RestfulNexus.NEXUS.getAccount(player.credentials.username);

				// New account
				if (account == null) {
					//if (checkPassword) {
					LoginCode response = PunishmentHandler.getBlockResponse(player);
					if (response != LoginCode.NORMAL) {
						return response;
					}
					//}

					String salt = IpsBCrypt.gensalt();
					String hash = IpsBCrypt.hashpw(player.credentials.password, salt);

					boolean success = false;
					for (int attempt = 0; attempt < 5; attempt++) {
						if (RestfulNexus.NEXUS.addAccount(player.credentials.username, hash, salt, player.getSession().hostAddress)) {
							success = true;
							break;
						}
						Thread.sleep(1_000);
					}

					if (!success) {
						return LoginCode.LOGIN_SERVER_OFFLINE;
					}

					player.credentials.password = hash;

					player.firstLogin = true;
					player.getAttributeMap().set(PlayerAttributes.ACCOUNT_CREATION_EPOCH, Instant.now(DateTimeUtil.CLOCK).toEpochMilli());
					World.get().getDatabaseWorker().submit(new DailyStatisticInsertTransaction(player.credentials.username, getHost(), player.firstLogin));
					return LoginCode.NORMAL;
				}

				ForumCredentials credentials = account.getForumCredentials();
				MultifactorAuthentication authentication = credentials.getAuthentication();
				String salt = credentials.getPasswordSalt();
				String hashed = credentials.getPasswordHash();
				player.credentials.databaseId = credentials.getId();

				// If we have a salt, verify using legacy method
				if (!Strings.isNullOrEmpty(salt)) {
					String password = IpsBCrypt.hashpw(player.credentials.password, salt);
					if (!password.equals(hashed)) {
						return LoginCode.INVALID_CREDENTIALS;
					}

					player.credentials.password = password;
				} else {
					// todo - Update salt version... PHP bcrypt uses 2y, jbcrypt uses 2a
					hashed = hashed.replace("2y", "2a");

					String password = BCrypt.hashpw(player.credentials.password, hashed);
					if (!password.equals(hashed)) {
						return LoginCode.INVALID_CREDENTIALS;
					}

					player.credentials.password = password;
				}

				if (authentication != null) {
					player.getAttributeMap().set(PlayerAttributes.HAS_AUTHENTICATOR, true);
					player.setMfaSecret(authentication.getSecret());
				}

				player.credentials.setEmail(new PlayerEmail(credentials.getEmail()));
			} catch (Exception cause) {
				LOGGER.error("Unable to load data for player: " + player, cause);
				return LoginCode.LOGIN_SERVER_OFFLINE;
			}
		}


		// Deserialization
		if(code == LoginCode.NORMAL) {
			player.credentials.setUsername(request.getUsername());
			code = PlayerPersistenceManager.load(player);
		}

		//future.awaitUninterruptibly();
		return code;
	}

	private void handleLoginRequest(ChannelHandlerContext ctx, LoginRequest request) {

		LoginCode code = handleRequest(request);

		ChannelFuture future = channel.writeAndFlush(new LoginResponse(code, player.getRights(), player.isIronMan()).toBuf(ctx));
		if(code != LoginCode.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		ctx.pipeline().addFirst("encoder", new GameEncoder(request.getEncryptor(), player));
		ctx.pipeline().addBefore("handler", "decoder", new GameDecoder(request.getDecryptor()));

		World.get().queueLogin(player);
	}

	/**
	 * Writes a closed response to the login channel.
	 */
	public static void write(ChannelHandlerContext ctx, LoginCode response) {
		Channel channel = ctx.channel();
		LoginResponse message = new LoginResponse(response);
		if(response == LoginCode.NORMAL) {
			ByteBuf initialMessage = releaseLater(ctx.alloc().buffer(9));
			initialMessage.writeLong(0); // Write initial message.
			channel.write(initialMessage, channel.voidPromise());
		}
		channel.writeAndFlush(message.toBuf(ctx)).addListener(ChannelFutureListener.CLOSE); // Write response message.
	}
	
	/**
	 * Gets the host address from the channel.
	 * @param ctx channel.
	 * @return host address as string.
	 */
	static String address(ChannelHandlerContext ctx) {
		return ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
	}

	/**
	 * Sets the id for this session.
	 * @param sessionId the id.
	 */
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Getting a {@link ByteBufAllocator} to allocate buffers.
	 * @return allocator.
	 */
	public ByteBufAllocator alloc() {
		return getChannel().alloc();
	}
	
	/**
	 * Gets the host address of this session.
	 * @return host address.
	 */
	public String getHost() {
		return hostAddress;
	}

	public String getUid() {
		return uid;
	}

	/**
	 * And identifier used to identify this specific session and tie
	 * and ingame events to it.
	 * @return the session id.
	 */
	public long getSessionId() {
		return sessionId;
	}

	/**
	 * Gets the channel of this session.
	 * @return channel.
	 */
	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * Gets the mac address of this session.
	 * @return
	 */
	public String getMacAddress() {
		return macAddress;
	}
	
	/**
	 * Returns {@code true} if mac is valid.
	 * @param mac mac address to verify.
	 * @return if mac is valid.
	 */
	public static boolean validMac(String mac) {
		return !mac.equals(NetworkConstants.INVALID_MAC);
	}

	public Duration getSessionDuration() {
		return sessionStart.elapsed();
	}
}