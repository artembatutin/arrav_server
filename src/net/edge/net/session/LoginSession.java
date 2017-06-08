package net.edge.net.session;

import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.edge.net.NetworkConstants;
import net.edge.net.codec.game.GameMessageDecoder;
import net.edge.net.codec.game.GameMessageEncoder;
import net.edge.net.codec.login.LoginRequest;
import net.edge.net.codec.login.LoginResponse;
import net.edge.net.codec.login.LoginResponseMessage;
import net.edge.util.TextUtils;
import net.edge.game.GameConstants;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;

/**
 * A {@link Session} implementation that handles networking for a {@link Player} during login.
 * @author lare96 <http://github.org/lare96>
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
	
	/**
	 * Handles a {@link LoginRequest}.
	 * @param request The message containing the credentials.
	 * @throws Exception If any errors occur while handling credentials.
	 */
	private void handleRequest(final LoginRequest request) throws Exception {
		Player player = new Player(TextUtils.nameToHash(request.getUsername()));
		LoginResponse response = LoginResponse.NORMAL;
		Channel channel = getChannel();
		
		// Validate the username and password, change login response if needed
		// for invalid credentials or the world being full.
		boolean invalidCredentials = !request.getUsername().matches("^[a-zA-Z0-9_ ]{1,12}$") || request.getPassword().isEmpty() || request.getPassword().length() > 20;
		response = invalidCredentials ? LoginResponse.INVALID_CREDENTIALS : World.get().getPlayers().remaining() == 0 ? LoginResponse.WORLD_FULL : response;
		
		// Validating login before deserialization.
		if(response == LoginResponse.NORMAL) {
			player.setUsername(request.getUsername().toLowerCase());
			player.setPassword(request.getPassword());
			if(World.get().getPlayer(player.getUsernameHash()).isPresent()) {
				response = LoginResponse.ACCOUNT_ONLINE;
			}
			if(request.getBuild() != GameConstants.CLIENT_BUILD) {
				response = LoginResponse.WRONG_BUILD_NUMBER;
			}
		}
		
		// Deserialization
		PlayerSerialization.SerializeResponse serial = null;
		if(response == LoginResponse.NORMAL) {
			serial = new PlayerSerialization(player).loginCheck(request.getPassword());
			response = serial.getResponse();
		}
		
		ChannelFuture future = channel.writeAndFlush(new LoginResponseMessage(response, player.getRights()));
		if(response != LoginResponse.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			final JsonObject reader = serial.getReader();
			future.addListener(it -> {
				request.getPipeline().replace("login-encoder", "game-encoder", new GameMessageEncoder(request.getEncryptor()));
				request.getPipeline().replace("login-decoder", "game-decoder", new GameMessageDecoder(request.getDecryptor()));
				
				GameSession session = new GameSession(player, channel, request.getEncryptor(), request.getDecryptor());
				
				channel.attr(NetworkConstants.SESSION_KEY).set(session);
				player.setSession(session);
				
				new PlayerSerialization(player).deserialize(reader);
				World.get().queueLogin(player);
			});
		}
	}
	
	public void handleResponse(Player player, LoginRequest request, LoginResponse response) {
		System.out.println(player.getUsername() + " - " + response.toString());
		Channel channel = getChannel();
		channel.writeAndFlush(new LoginResponseMessage(response, player.getRights()));
		
		
		if(response != LoginResponse.NORMAL) {
			channel.close();
		} else {
			request.getPipeline().replace("login-encoder", "game-encoder", new GameMessageEncoder(request.getEncryptor()));
			request.getPipeline().replace("login-decoder", "game-decoder", new GameMessageDecoder(request.getDecryptor()));
			
			GameSession session = new GameSession(player, channel, request.getEncryptor(), request.getDecryptor());
			
			channel.attr(NetworkConstants.SESSION_KEY).set(session);
			player.setSession(session);
			
			World.get().queueLogin(player);
		}
	}
	
}