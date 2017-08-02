package net.edge.net.session;

import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.edge.net.NetworkConstants;
import net.edge.net.codec.login.LoginRequest;
import net.edge.net.codec.login.LoginCode;
import net.edge.net.codec.login.LoginResponse;
import net.edge.GameConstants;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.PlayerCredentials;
import net.edge.world.entity.actor.player.PlayerSerialization;

/**
 * A {@link Session} implementation that handles networking for a {@link Player} during login.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class LoginSession extends Session {
	
	
	/**
	 * Creates a new {@link LoginSession}.
	 * @param channel The {@link Channel} for this session.
	 */
	public LoginSession(Channel channel) {
		super(channel);
	}
	
	@Override
	public void handleUpstreamMessage(Object msg) throws Exception {
		if(msg instanceof LoginRequest) {
			LoginRequest request = (LoginRequest) msg;
			handleRequest(request);
		}
	}
	
	@Override
	public void terminate() {
	
	}
	
	@Override
	public Player getPlayer() {
		return null;
	}
	
	/**
	 * Handles a {@link LoginRequest}.
	 * @param request The message containing the credentials.
	 * @throws Exception If any errors occur while handling credentials.
	 */
	private void handleRequest(final LoginRequest request) throws Exception {
		Player player = new Player(new PlayerCredentials(request.getUsername(), request.getPassword()), true);
		LoginCode response = LoginCode.NORMAL;
		Channel channel = getChannel();
		
		// Validate the username and password, change login response if needed
		// for invalid credentials or the world being full.
		boolean invalidCredentials = !request.getUsername().matches("^[a-zA-Z0-9_ ]{1,12}$") || request.getPassword().isEmpty() || request.getPassword().length() > 20;
		response = invalidCredentials ? LoginCode.INVALID_CREDENTIALS : World.get().getPlayers().remaining() == 0 ? LoginCode.WORLD_FULL : response;
		
		// Validating login before deserialization.
		if(response == LoginCode.NORMAL) {
			player.getCredentials().setUsername(request.getUsername());
			player.getCredentials().setPassword(request.getPassword());
			if(World.get().getPlayer(player.getCredentials().getUsernameHash()).isPresent()) {
				response = LoginCode.ACCOUNT_ONLINE;
			}
			if(request.getBuild() != GameConstants.CLIENT_BUILD) {
				response = LoginCode.WRONG_BUILD_NUMBER;
			}
		}

		// Deserialization
		PlayerSerialization.SerializeResponse serial = null;
		if(response == LoginCode.NORMAL) {
			serial = new PlayerSerialization(player).loginCheck(request.getPassword());
			response = serial.getResponse();
		}
		
		ChannelFuture future = channel.writeAndFlush(new LoginResponse(response, player.getRights(), player.isIronMan()));
		if(response != LoginCode.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}
		future.awaitUninterruptibly();
		
		final JsonObject reader = serial.getReader();
		GameSession session = new GameSession(player, channel, request.getEncryptor(), request.getDecryptor());
		channel.attr(NetworkConstants.SESSION_KEY).set(session);
		player.setSession(session);
		World.get().run(() -> {
			new PlayerSerialization(player).deserialize(reader);
			World.get().queueLogin(player);
		});
	}
	
}