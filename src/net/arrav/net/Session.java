package net.arrav.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import net.arrav.Arrav;
import net.arrav.GameConstants;
import net.arrav.net.codec.crypto.IsaacRandom;
import net.arrav.net.codec.game.GameDecoder;
import net.arrav.net.codec.game.GameEncoder;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.codec.game.GameState;
import net.arrav.net.codec.login.LoginCode;
import net.arrav.net.codec.login.LoginRequest;
import net.arrav.net.codec.login.LoginResponse;
import net.arrav.net.codec.login.LoginState;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.net.packet.out.SendLogout;
import net.arrav.util.TextUtils;
import net.arrav.world.World;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.PlayerCredentials;
import net.arrav.world.entity.actor.player.PlayerSerialization;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Player's session which handles I/O operations.
 * @author Artem Batutin
 */
public class Session {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(Session.class.getName());
	
	/**
	 * The cap limit of outgoing packets per session.
	 */
	public static int UPDATE_LIMIT = 200;
	
	/**
	 * The ip address that the connection was received from.
	 */
	private final String hostAddress;
	
	/**
	 * The mac address the connection was received from.
	 */
	private String macAddress;
	
	/**
	 * The {@link Channel} to send and receive messages through.
	 */
	private final Channel channel;
	
	/**
	 * The player associated with this session.
	 */
	private Player player;
	
	/**
	 * The queue of {@link ByteBuf}s.
	 */
	private BlockingQueue<ByteBuf> messages = new ArrayBlockingQueue<>(NetworkConstants.MESSAGES_PER_TICK);
	
	/**
	 * The queue of {@link ByteBuf}s.
	 */
	private Queue<OutgoingPacket> outgoing = new ConcurrentLinkedQueue<>();
	
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
		if(msg instanceof LoginRequest) {
			handleRequest(ctx, (LoginRequest) msg);
		}
		if(msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			if(buf.getOpcode() > 0) {
				if(messages.size() < NetworkConstants.MESSAGES_PER_TICK) {
					messages.add(buf);
				}
			}
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
		if(messages != null) {
			for(ByteBuf b : messages) {
				b.release();
			}
			messages.clear();
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
	public void handle(ByteBuf packet) {
		try {
			NetworkConstants.MESSAGES[packet.getOpcode()].handle(player, packet.getOpcode(), packet.capacity(), packet);
		} finally {
			packet.release();
		}
	}
	
	/**
	 * Polling all incoming packets.
	 */
	public void pollIncomingMessages() {
		while(!messages.isEmpty()) {
			ByteBuf msg = messages.poll();
			handle(msg);
		}
	}
	
	/**
	 * Writes the given {@link OutgoingPacket} to the stream.
	 */
	public void writeUpdate(OutgoingPacket playerUpdate, OutgoingPacket mobUpdate) {
		if(channel.isActive() && channel.isOpen()) {
			channel.write(playerUpdate);
			channel.write(mobUpdate);
			while(!outgoing.isEmpty()) {
				OutgoingPacket packet = outgoing.poll();
				ChannelFuture future = channel.write(packet);
				if(packet.getClass() == SendLogout.class) {
					future.addListener(ChannelFutureListener.CLOSE);
				}
			}
		}
	}
	
	public void write(OutgoingPacket packet) {
		outgoing.add(packet);
	}
	
	public void flush() {
		channel.flush();
	}
	
	/**
	 * Handles a login request.
	 * @throws Exception If any errors occur while handling credentials.
	 */
	private void handleRequest(ChannelHandlerContext ctx, LoginRequest request) throws Exception {
		this.macAddress = request.getMacAddress();
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
		// Deserialization
		if(code == LoginCode.NORMAL) {
			player.credentials.setUsername(request.getUsername());
			player.credentials.password = request.getPassword();
			code = new PlayerSerialization(player).loginCheck(request.getPassword());
		}
		ChannelFuture future = channel.writeAndFlush(new LoginResponse(code, player.getRights(), player.isIronMan()).toBuf(ctx));
		if(code != LoginCode.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		//future.awaitUninterruptibly();
		
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
			ByteBuf initialMessage = ctx.alloc().buffer(9).writeLong(0); // Write initial message.
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
}