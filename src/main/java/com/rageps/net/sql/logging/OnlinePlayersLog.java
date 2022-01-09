package com.rageps.net.sql.logging;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.env.Environment;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by Ryley Kimmel on 1/12/2017.
 */
public final class OnlinePlayersLog extends DatabaseTransaction {
	private final Player player;

	private final Type type;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public OnlinePlayersLog(Player player, Type type) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.type = type;
	}

	public static OnlinePlayersLog createLoginLog(Player player) {
		return new OnlinePlayersLog(player, Type.LOGIN);
	}

	public static OnlinePlayersLog createLogoutLog(Player player) {
		return new OnlinePlayersLog(player, Type.LOGOUT);
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		if (World.get().getEnvironment().getType() != Environment.Type.LIVE) {
			return;
		}
		switch (type) {
			case LOGIN:
				try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT INTO online_players (username, ip_address, uid, timestamp) VALUES (:username, :ip_address, :uid, :timestamp);")) {
					statement.setString("username", player.credentials.username);
					statement.setString("ip_address", player.credentials.getHostAddress());
					statement.setString("uid", player.credentials.getUid());
					statement.setTimestamp("timestamp", timestamp);
					statement.executeUpdate();
				}

				try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT INTO logins (username, ip_address, uid, timestamp) VALUES (:username, :ip_address, :uid, :timestamp)")) {
					statement.setString("username", player.credentials.username);
					statement.setString("ip_address", player.credentials.getHostAddress());
					statement.setString("uid", player.credentials.getUid());
					statement.setTimestamp("timestamp", timestamp);
					statement.executeUpdate();
				}

				connection.commit();
				return;

			case LOGOUT:
				try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "DELETE FROM online_players WHERE username=:username;")) {
					statement.setString("username", player.credentials.username);
					statement.executeUpdate();
				}

				try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT INTO logouts (username, ip_address, uid, session_duration, timestamp) VALUES (:username, :ip_address, :uid, :session_duration, :timestamp);")) {
					statement.setString("username", player.credentials.username);
					statement.setString("ip_address", player.credentials.getHostAddress());
					statement.setString("uid", player.credentials.getUid());
					statement.setLong("session_duration", player.getAttributeMap().getLong(PlayerAttributes.SESSION_DURATION));
					statement.setTimestamp("timestamp", timestamp);
					statement.executeUpdate();
				}

				connection.commit();
				return;

			default:
				throw new IllegalArgumentException(type + " is not valid!");
		}
	}

	private enum Type {
		LOGIN,
		LOGOUT
	}
}
