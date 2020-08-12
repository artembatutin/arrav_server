package com.rageps.net.refactor.session.impl;

import com.rageps.GameConstants;
import com.rageps.net.refactor.message.Message;
import com.rageps.net.refactor.message.MessageHandlerChainSet;
import com.rageps.net.refactor.message.impl.LogoutMessage;
import com.rageps.net.refactor.session.Session;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	 * The queue of pending {@link Message}s.
	 */
	private final BlockingQueue<Message> messages = new ArrayBlockingQueue<>(GameConstants.MESSAGES_PER_PULSE);

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * If the player was reconnecting.
	 */
	private final boolean reconnecting;

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
	 * @param message The message.
	 */
	public void dispatchMessage(Message message) {
		Channel channel = getChannel();
		if (channel.isActive() && channel.isOpen()) {
			ChannelFuture future = channel.writeAndFlush(message);
			if (message.getClass() == LogoutMessage.class) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	/**
	 * Handles pending messages for this session.
	 *
	 * @param chainSet The {@link MessageHandlerChainSet}
	 */
	public void handlePendingMessages(MessageHandlerChainSet chainSet) {
		while (!messages.isEmpty()) {
			Message message = messages.poll();

			try {
				chainSet.notify(player, message);
			} catch (Exception reason) {
				logger.fatal("Uncaught exception thrown while handling message: {}", message, reason);
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
		if (messages.size() >= GameConstants.MESSAGES_PER_PULSE) {
			logger.warn("Too many messages in queue for game session, dropping...");
		} else {
			messages.add((Message) message);
		}
	}

}