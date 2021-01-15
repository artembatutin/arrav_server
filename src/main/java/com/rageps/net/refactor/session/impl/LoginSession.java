package com.rageps.net.refactor.session.impl;

import com.rageps.net.refactor.codec.game.GameMessageDecoder;
import com.rageps.net.refactor.codec.game.GameMessageEncoder;
import com.rageps.net.refactor.codec.game.GamePacketDecoder;
import com.rageps.net.refactor.codec.game.GamePacketEncoder;
import com.rageps.net.refactor.codec.login.LoginConstants;
import com.rageps.net.refactor.codec.login.LoginRequest;
import com.rageps.net.refactor.codec.login.LoginResponse;
import com.rageps.net.refactor.release.Release;
import com.rageps.net.refactor.security.IsaacRandomPair;
import com.rageps.net.refactor.session.Session;
import com.rageps.service.impl.GameService;
import com.rageps.service.impl.LoginService;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.persist.PlayerLoaderResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.io.IOException;
import java.util.Optional;

/**
 * A login session.
 *
 * @author Graham
 */
public final class LoginSession extends Session {

	/**
	 * The LoginRequest for this LoginSession.
	 */
	private LoginRequest request;

	/**
	 * Creates a login session for the specified channel.
	 *
	 * @param channel The channel.
	 */
	public LoginSession(Channel channel) {
		super(channel);
	}

	@Override
	public void destroy() {
		System.out.println("destroy");
	}

	/**
	 * Handles a response from the login service.
	 *
	 * @param request The request this response corresponds to.
	 * @param response The response.
	 */
	public void handlePlayerLoaderResponse(LoginRequest request, PlayerLoaderResponse response) {

		this.request = request;
		GameService service = World.get().getGameService();
		Optional<Player> optional = response.getPlayer();

		if (optional.isPresent()) {
			service.registerPlayer(optional.get(), this);
		} else {
			sendLoginFailure(response.getStatus());
		}
	}

	@Override
	public void messageReceived(Object message) throws Exception {
		if (message.getClass() == LoginRequest.class) {
			handleLoginRequest((LoginRequest) message);
		}
	}

	/**
	 * Sends a failed {@link LoginResponse} to the client.
	 *
	 * @param status The failure status.
	 */
	public void sendLoginFailure(int status) {
		System.out.println("login failure "+status);

		boolean flagged = false;
		LoginResponse response = new LoginResponse(status, Rights.PLAYER, flagged);
		channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Sends a succesfull {@link LoginResponse} to the client.
	 *
	 * @param player The {@link Player} that successfully logged in.
	 */
	public void sendLoginSuccess(Player player) {
		IsaacRandomPair randomPair = request.getRandomPair();
		boolean flagged = false;

		Release release = World.get().getRelease();

		GameSession session = new GameSession(channel, player, request.isReconnecting());
		channel.attr(ApolloHandler.SESSION_KEY).set(session);
		player.setSession(session);

		channel.writeAndFlush(new LoginResponse(LoginConstants.STATUS_OK, player.getRights(), flagged));

		channel.pipeline().addFirst("messageEncoder", new GameMessageEncoder(release));
		channel.pipeline().addBefore("messageEncoder", "gameEncoder", new GamePacketEncoder(randomPair.getEncodingRandom()));

		channel.pipeline().addBefore("handler", "gameDecoder",
				new GamePacketDecoder(randomPair.getDecodingRandom(), release));
		channel.pipeline().addAfter("gameDecoder", "messageDecoder", new GameMessageDecoder(release));

		channel.pipeline().remove("loginDecoder");
		channel.pipeline().remove("loginEncoder");
	}

	/**
	 * Handles a login request.
	 *
	 * @param request The login request.
	 * @throws IOException If some I/O exception occurs.
	 */
	private void handleLoginRequest(LoginRequest request) throws IOException {
		LoginService service = World.get().getLoginService();
		service.submitLoadRequest(this, request);
	}
}