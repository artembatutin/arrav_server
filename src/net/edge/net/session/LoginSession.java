package net.edge.net.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.edge.net.NetworkConstants;
import net.edge.net.PunishmentHandler;
import net.edge.net.codec.game.GameMessageDecoder;
import net.edge.net.codec.game.GameMessageEncoder;
import net.edge.net.codec.login.LoginCredentialsMessage;
import net.edge.net.codec.login.LoginResponse;
import net.edge.net.codec.login.LoginResponseMessage;
import net.edge.utils.TextUtils;
import net.edge.GameConstants;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;
import net.edge.world.node.entity.player.assets.Rights;

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
		if(msg instanceof LoginCredentialsMessage) {
			LoginCredentialsMessage credentials = (LoginCredentialsMessage) msg;
			handleCredentials(credentials);
		}
	}
	
	/**
	 * Loads the character file and sends the {@link LoginResponse} code to the client.
	 * @param msg The message containing the credentials.
	 * @throws Exception If any errors occur while handling credentials.
	 */
	private void handleCredentials(LoginCredentialsMessage msg) throws Exception {
		Player player = new Player(TextUtils.nameToHash(msg.getUsername()));
		Channel channel = getChannel();
		LoginResponse response = LoginResponse.NORMAL;
		
		// Validate the username and password, change login response if needed
		// for invalid credentials or the world being full.
		boolean invalidCredentials = !msg.getUsername().matches("^[a-zA-Z0-9_ ]{1,12}$") || msg.getPassword().isEmpty() || msg.getPassword().length() > 20;
		response = invalidCredentials ? LoginResponse.INVALID_CREDENTIALS : World.getPlayers().remaining() == 0 ? LoginResponse.WORLD_FULL : response;
		
		// If the login response is normal, deserialize the character file (or
		// grab it from the Cache if it was recently serialized).
		if(response == LoginResponse.NORMAL) {
			player.setUsername(msg.getUsername().toLowerCase());
			player.setPassword(msg.getPassword());
			if(World.getPlayer(player.getUsernameHash()).isPresent()) {
				response = LoginResponse.ACCOUNT_ONLINE;
			}
			if(msg.getBuild() != GameConstants.CLIENT_BUILD) {
				response = LoginResponse.WRONG_BUILD_NUMBER;
			}
		}
		
		if(response == LoginResponse.NORMAL) {
			response = new PlayerSerialization(player).deserialize(msg.getPassword(), true);
		}
		player.setRights(PunishmentHandler.isLocal(getHost()) ? Rights.DEVELOPER : player.getRights());
		
		ChannelFuture future = channel.writeAndFlush(new LoginResponseMessage(response, player.getRights()));
		if(response != LoginResponse.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			future.addListener(it -> {
				msg.getPipeline().replace("login-encoder", "game-encoder", new GameMessageEncoder(msg.getEncryptor()));
				msg.getPipeline().replace("login-decoder", "game-decoder", new GameMessageDecoder(msg.getDecryptor()));
				
				GameSession session = new GameSession(player, channel, msg.getEncryptor(), msg.getDecryptor());
				
				channel.attr(NetworkConstants.SESSION_KEY).set(session);
				player.setSession(session);
				
				new PlayerSerialization(player).deserialize(msg.getPassword(), false);
				World.queueLogin(player);
			});
		}
	}
	
}