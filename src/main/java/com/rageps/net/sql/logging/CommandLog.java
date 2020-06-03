package com.rageps.net.sql.logging;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.locale.Position;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by Ryley Kimmel on 11/4/2016.
 */
public final class CommandLog extends DatabaseTransaction {
	private final Player player;

	private final Position position;

	private final String command;

	private final String arguments;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public CommandLog(Player player, String command, String arguments) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.position = player.getPosition();
		this.command = command;
		this.arguments = arguments;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO commands (username, ip_address, uid, command, arguments, x, y, z, timestamp) VALUES (:username, :ip_address, :uid, :command, :arguments, :x, :y, :z, :timestamp);")) {
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.getSession().getHost());
			statement.setString("uid", player.getSession().getUid());
			statement.setString("command", command);
			statement.setString("arguments", arguments);
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
