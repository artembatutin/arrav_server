package com.rageps.net.refactor.session.impl;

import com.google.common.base.Stopwatch;
import com.rageps.GameConstants;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.PacketHandlerChainSet;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.net.refactor.session.Session;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A game session.
 *
 * @author Graham
 */
public final class GameSession extends Session {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger();


	/**
	 * The queue of pending {@link Packet}s.
	 */
	private final BlockingQueue<Packet> packets = new ArrayBlockingQueue<>(GameConstants.MESSAGES_PER_PULSE);

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * If the player was reconnecting.
	 */
	private final boolean reconnecting;

	/**
	 * A unique identification associated with this {@link GameSession}.
	 */
	private long sessionId;

	/**
	 * A stopwatch to measure the length of the session.
	 */
	private final Stopwatch sessionStart = Stopwatch.createStarted();

	/**
	 * Creates a login session for the specified channel.
	 *
	 * @param channel The channel.
	 * @param player The player.
	 * @param reconnecting If the player was reconnecting.
	 */
	public GameSession(Channel channel, Player player, boolean reconnecting) {
		super(channel);
		this.player = player;
		this.reconnecting = reconnecting;
	}

	@Override
	public void destroy() {
		World.get().getGameService().unregisterPlayer(player);
	}

	/**
	 * Encodes and dispatches the specified message.
	 *
	 * @param packet The message.
	 */
	public void dispatchMessage(Packet packet) {
		Channel channel = getChannel();
		if (channel.isActive() && channel.isOpen()) {
			ChannelFuture future = channel.writeAndFlush(packet);
			if (packet.getClass() == LogoutPacket.class) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	/**
	 * Handles pending messages for this session.
	 *
	 * @param chainSet The {@link PacketHandlerChainSet}
	 */
	public void handlePendingMessages(PacketHandlerChainSet chainSet) {
		while (!packets.isEmpty()) {
			Packet packet = packets.poll();

			try {
				chainSet.notify(player, packet);
			} catch (Exception reason) {
				logger.fatal("Uncaught exception thrown while handling message: {}", packet, reason);
			}
		}
	}

	/**
	 * Handles a player saver response.
	 *
	 * @param success A flag indicating if the save was successful.
	 */
	public void handlePlayerSaverResponse(boolean success) {
		World.get().getGameService().finalizePlayerUnregistration(player);
	}

	/**
	 * Determines if this player is reconnecting.
	 *
	 * @return {@code true} if reconnecting, {@code false} otherwise.
	 */
	public boolean isReconnecting() {
		return reconnecting;
	}

	@Override
	public void messageReceived(Object message) {
		if (packets.size() >= GameConstants.MESSAGES_PER_PULSE) {
			logger.warn("Too many messages in queue for game session, player={} dropping...", player.credentials.username);
		} else {
			packets.add((Packet) message);
		}
	}

	/**
	 * And identifier used to identify this specific session and tie
	 * and ingame events to it.
	 * @return the session id.
	 */
	public long getSessionId() {
		return sessionId;
	}

	public Duration getSessionDuration() {
		return sessionStart.elapsed();
	}
}