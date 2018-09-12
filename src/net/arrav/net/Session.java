package net.arrav.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.arrav.Arrav;
import net.arrav.GameConstants;
import net.arrav.net.codec.crypto.IsaacRandom;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.codec.game.GameState;
import net.arrav.net.codec.login.LoginCode;
import net.arrav.net.codec.login.LoginResponse;
import net.arrav.net.codec.login.LoginState;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.net.packet.OutgoingPacket;
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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Player's session which handles I/O operations.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Session {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(Session.class.getName());
	
	/**
	 * Game stream capacity.
	 */
	private final static int BUFFER_SIZE = 5000;
	/**
	 * The cap limit of outgoing packets per session.
	 */
	public static int UPDATE_LIMIT = 200;
	/**
	 * Invalid constants of an unknown mac address.
	 */
	private static String INVALID_MAC = "0";
	
	/**
	 * The {@link Channel} to send and receive messages through.
	 */
	private final Channel channel;
	/**
	 * The ip address that the connection was received from.
	 */
	private final String hostAddress;
	/**
	 * The mac address the connection was received from.
	 */
	private String macAddress;
	
	/**
	 * Condition if session is handling game process.
	 */
	private boolean isGame;
	/**
	 * Condition if this session is active.
	 */
	private boolean active = true;
	
	/**
	 * The player's username hash received from client.
	 */
	private long usernameHash;
	/**
	 * The player's username on login.
	 */
	private String username;
	/**
	 * The player's password on login.
	 */
	private String password;
	/**
	 * The player associated with this session.
	 */
	private Player player;
	
	/* Incoming and outgoing packet queues. */
	/**
	 * The queue of {@link ByteBuf}s.
	 */
	private Queue<ByteBuf> incoming;
	/**
	 * The queue of {@link OutgoingPacket}s.
	 */
	private Queue<OutgoingPacket> outgoing;
	
	/* ISAAC cypher. */
	/**
	 * The message encryptor.
	 */
	private IsaacRandom encryptor;
	/**
	 * The message decryptor.
	 */
	private IsaacRandom decryptor;
	
	
	/* Login request decoding. */
	/**
	 * A cryptographically secure random number generator.
	 */
	private static final Random RANDOM = new SecureRandom();
	/**
	 * The current state of decoding the protocol.
	 */
	private LoginState loginState = LoginState.HANDSHAKE;
	/**
	 * The size of the last portion of the protocol.
	 */
	private int rsaBlockSize;
	
	/* Game packet decoding. */
	/**
	 * The number of skipped responded cycles.
	 */
	private AtomicInteger skippedCycles = new AtomicInteger();
	/**
	 * The state of the message currently being decoded.
	 */
	private GameState gameState = GameState.OPCODE;
	/**
	 * The opcode of the message currently being decoded.
	 */
	private int opcode = -1;
	/**
	 * The size of the message currently being decoded.
	 */
	private int size = -1;
	/**
	 * The type of the message currently being decoded.
	 */
	private GamePacketType type = GamePacketType.RAW;
	/**
	 * The main game stream buffer to encode all outgoing packets.
	 */
	private ByteBuf outBuf;
	/**
	 * The main game stream buffer to decode all incoming data.
	 */
	private ByteBuf inBuf;
	
	/**
	 * Creates a new {@link Session}.
	 * @param channel The {@link Channel} to send and receive messages through.
	 */
	Session(Channel channel) {
		this.channel = channel;
		this.hostAddress = channel == null ? "" : ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
		inBuf = channel.alloc().buffer(256);
	}
	
	/**
	 * Handles an incoming message from the channel.
	 * @param msg message incoming.
	 * @throws Exception exception
	 */
	void handleMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		inBuf.writeBytes(in);
		try {
			if(isGame) {
				game();
			} else {
				login(ctx);
			}
		} finally {
			in.release();
			if(inBuf.readableBytes() < 1) {
				inBuf.clear();
			}
		}
	}
	
	/**
	 * Disposes of this {@code Session} by closing the {@link Channel}.
	 */
	void terminate() {
		if(player != null) {
			if(isGame && player.getState() != EntityState.AWAITING_REMOVAL && player.getState() != EntityState.INACTIVE) {
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
		if(outBuf != null) {
			if(outBuf.refCnt() != 0)
				outBuf.release(outBuf.refCnt());
		}
		if(inBuf != null) {
			if(inBuf.refCnt() != 0)
				inBuf.release(inBuf.refCnt());
		}
		if(incoming != null) {
			for(ByteBuf b : incoming) {
				b.release();
			}
			incoming.clear();
			outgoing.clear();
		}
		if(player != null) {
			LOGGER.info("Unregistered session for " + player.getFormatUsername());
		}
		player = null;
	}
	
	/**
	 * Enqueues the given {@link OutgoingPacket} for transport.
	 */
	public void enqueue(OutgoingPacket pkt) {
		outgoing.offer(pkt);
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
	public void pollIncomingPackets() {
		if(!incoming.isEmpty()) {
			int count = 0;
			while(!incoming.isEmpty() && count < 20) {
				ByteBuf msg = incoming.poll();
				handle(msg);
				count++;
			}
		}
	}
	
	/**
	 * Polling all outgoing packets.
	 */
	public void pollOutgoingPackets() {
		if(!outgoing.isEmpty()) {
			int count = 0;
			while(!outgoing.isEmpty() && count < UPDATE_LIMIT) {
				OutgoingPacket pkt = outgoing.poll();
				if(channel.isWritable()) {
					write(pkt);
				} else {
					outgoing.offer(pkt);
				}
				count++;
			}
		}
	}
	
	/**
	 * Writes the given {@link OutgoingPacket} to the stream.
	 */
	public void write(OutgoingPacket packet) {
		if(channel.isActive() && channel.isRegistered()) {
			packet.write(player, outBuf);
		}
		if (!Arrav.DEBUG && skippedCycles.incrementAndGet() > NetworkConstants.SESSION_TIMEOUT_CYCLE_COUNT) {
			if (player != null && player.getState() == EntityState.ACTIVE) {
				World.get().getPlayers().remove(player);
			}
		}
	}
	
	/**
	 * Flushes all pending {@link ByteBuf}s within the channel's queue. Repeated calls to this method are relatively
	 * expensive, which is why messages should be queued up with {@code queue(MessageWriter)} and flushed once at the end of
	 * the cycle.
	 */
	public void flushQueue() {
		if(!active)
			return;
		Channel channel = getChannel();
		if(channel.isActive()) {
			channel.eventLoop().execute(() -> {
				channel.writeAndFlush(outBuf.retain(), channel.voidPromise());
				outBuf.clear();
			});
		}
	}
	
	private void game() {
		switch(gameState) {
			case OPCODE:
				opcode();
				break;
			case SIZE:
				size();
				break;
			case PAYLOAD:
				payload();
				break;
		}
	}
	
	/**
	 * Decodes the opcode of the {@link ByteBuf}.
	 */
	private void opcode() {
		if(inBuf.isReadable()) {
			opcode = inBuf.readUnsignedByte();
			opcode = (opcode - decryptor.nextInt()) & 0xFF;
			size = NetworkConstants.MESSAGE_SIZES[opcode];
			skippedCycles.set(0);
			if(size == -1) {
				type = GamePacketType.VARIABLE_BYTE;
			} else if(size == -2) {
				type = GamePacketType.VARIABLE_SHORT;
			} else {
				type = GamePacketType.FIXED;
			}
			
			if(opcode == 0) {
				//reset timeout.
				opcode();
				return;
			}
			
			if(size == 0) {
				if(NetworkConstants.MESSAGES[opcode] != null)
					queueMessage(Unpooled.EMPTY_BUFFER);
				opcode();
				return;
			}
			
			gameState = size == -1 || size == -2 ? GameState.SIZE : GameState.PAYLOAD;
			if(gameState == GameState.SIZE) {
				size();
			} else {
				payload();
			}
		}
	}
	
	/**
	 * Decodes the size of the {@link ByteBuf}.
	 */
	private void size() {
		int bytes = size == -1 ? Byte.BYTES : Short.BYTES;
		if(inBuf.isReadable(bytes)) {
			size = 0;
			for(int i = 0; i < bytes; i++) {
				size |= inBuf.readUnsignedByte() << 8 * (bytes - 1 - i);
			}
			gameState = GameState.PAYLOAD;
			payload();
		}
	}
	
	/**
	 * Decodes the payload of the {@link ByteBuf}.
	 */
	private void payload() {
		if(inBuf.isReadable(size)) {
			if(NetworkConstants.MESSAGES[opcode] == null) {
				inBuf.skipBytes(size);//skip bytes.
				LOGGER.info("Skipped unhandled packet " + opcode + " - " + size);
				resetMessage();
				opcode();
			} else {
				ByteBuf newBuffer = inBuf.readBytes(size);
				queueMessage(newBuffer);
				opcode();
			}
		}
	}
	
	/**
	 * Prepares a {@link ByteBuf} to be queued upstream and handled on the main game thread.
	 * @param payload The payload of the {@code Packet}.
	 */
	private void queueMessage(ByteBuf payload) {
		try {
			ByteBuf packet = payload.incoming(opcode, type);
			if(packet.getOpcode() != 0) {
				if(packet.getOpcode() == 41) {
					handle(packet);//item equipping
					return;
				}
				incoming.offer(packet);
			}
		} finally {
			resetMessage();
		}
	}
	
	/**
	 * Resets the incoming message procedure.
	 */
	private void resetMessage() {
		opcode = -1;
		size = -1;
		gameState = GameState.OPCODE;
	}
	
	/**
	 * Handles a login request.
	 * @throws Exception If any errors occur while handling credentials.
	 */
	private void handleRequest(ChannelHandlerContext ctx) throws Exception {
		player = new Player(new PlayerCredentials(username, password));
		player.setSession(this);
		LoginCode code = LoginCode.NORMAL;
		
		// Validate the username and password, change login code if needed
		// for invalid credentials or the world being full.
		boolean invalidCredentials = !username.matches("^[a-zA-Z0-9_ ]{1,12}$") || password.isEmpty() || password.length() > 20;
		System.out.println("REMAINING SPACES: " + World.get().getPlayers().remaining());
		code = invalidCredentials ? LoginCode.INVALID_CREDENTIALS : World.get().getPlayers().remaining() < 1 ? LoginCode.WORLD_FULL : code;
		
		// Validating player login possibility.
		if(code == LoginCode.NORMAL && World.get().getPlayer(usernameHash).isPresent()) {
			code = LoginCode.ACCOUNT_ONLINE;
		}
		// Deserialization
		if(code == LoginCode.NORMAL) {
			player.credentials.setUsername(username);
			player.credentials.password = password;
			code = new PlayerSerialization(player).loginCheck(password);
		}
		ChannelFuture future = channel.writeAndFlush(new LoginResponse(code, player.getRights(), player.isIronMan()).toBuf(ctx));
		if(code != LoginCode.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}
		future.awaitUninterruptibly();
		World.get().queueLogin(player);
	}
	
	/**
	 * Decodes a login message;
	 * @param ctx channel context.
	 * @throws Exception ex.
	 */
	private void login(ChannelHandlerContext ctx) throws Exception {
		switch(loginState) {
			case HANDSHAKE:
				decodeHandshake(ctx);
				loginState = LoginState.LOGIN_BLOCK;
				break;
			case LOGIN_BLOCK:
				decodeLoginBlock(ctx);
		}
	}
	
	/**
	 * Decodes the handshake portion of the login protocol.
	 * @param ctx The channel handler context.
	 * @throws Exception If any exceptions occur while decoding this portion of the protocol.
	 */
	private void decodeHandshake(ChannelHandlerContext ctx) throws Exception {
		if(inBuf.readableBytes() >= 1) {
			int build = inBuf.get();
			if(build != GameConstants.CLIENT_BUILD) {
				write(ctx, LoginCode.WRONG_BUILD_NUMBER);
				return;
			}
			ByteBuf buf = ctx.alloc().buffer(9);
			buf.writeByte(0);
			buf.writeLong(RANDOM.nextLong());
			ctx.writeAndFlush(buf, ctx.voidPromise());
		}
	}
	
	/**
	 * Decodes the portion of the login protocol to sucessfully login.
	 * @param ctx The channel handler context.
	 * @throws Exception If any exceptions occur while decoding this portion of the protocol.
	 */
	private void decodeLoginBlock(ChannelHandlerContext ctx) throws Exception {
		if(rsaBlockSize == 0 && inBuf.readableBytes() >= 1) {
			//RSA size
			rsaBlockSize = inBuf.readUnsignedByte();
			if(rsaBlockSize == 0) {
				write(ctx, LoginCode.COULD_NOT_COMPLETE_LOGIN);
				return;
			}
		}
		if(inBuf.readableBytes() >= rsaBlockSize) {
			int expectedSize = inBuf.readUnsignedByte();
			if(expectedSize != rsaBlockSize - 1) {
				write(ctx, LoginCode.COULD_NOT_COMPLETE_LOGIN);
				return;
			}
			byte[] rsaBytes = new byte[rsaBlockSize - 1];
			inBuf.readBytes(rsaBytes);
			byte[] rsaData = new BigInteger(rsaBytes).toByteArray();
			ByteBuf rsaBuffer = Unpooled.wrappedBuffer(rsaData);
			try {
				long clientHalf = rsaBuffer.readLong();
				long serverHalf = rsaBuffer.readLong();
				int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf};
				decryptor = new IsaacRandom(isaacSeed);
				for(int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				encryptor = new IsaacRandom(isaacSeed);
				//mac address
				int macId = rsaBuffer.readInt();
				macAddress = String.valueOf(macId);
				if(validMac(macAddress) && HostManager.contains(macAddress, HostListType.BANNED_MAC)) {
					write(ctx, LoginCode.ACCOUNT_DISABLED);
					return;
				}
				username = rsaBuffer.getCString().toLowerCase().replaceAll("_", " ").toLowerCase().trim();
				password = rsaBuffer.getCString().toLowerCase();
				usernameHash = TextUtils.nameToHash(username);
				if(World.get().getPlayer(usernameHash).isPresent()) {
					write(ctx, LoginCode.ACCOUNT_ONLINE);
					return;
				}
				handleRequest(ctx);
			} finally {
				if(rsaBuffer.isReadable()) {
					rsaBuffer.release();
				}
			}
		}
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
	 * Prepares the session for the game process.
	 */
	public void initGame() {
		outBuf = alloc().buffer(BUFFER_SIZE);
		outBuf.outgoing(encryptor);
		outgoing = new ConcurrentLinkedQueue<>();
		incoming = new ConcurrentLinkedQueue<>();
		isGame = true;
	}
	
	/**
	 * Returns {@code true} if mac is valid.
	 * @param mac mac address to verify.
	 * @return if mac is valid.
	 */
	public static boolean validMac(String mac) {
		return !mac.equals(INVALID_MAC);
	}
	
	/**
	 * Getting a {@link ByteBufAllocator} to allocate buffers.
	 * @return allocator.
	 */
	public ByteBufAllocator alloc() {
		return getChannel().alloc();
	}
	
	/**
	 * Setting the connection condition.
	 * @param active flag.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Gets the mac address of this session.
	 * @return mac address.
	 */
	public String getMacAddress() {
		return macAddress;
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
}