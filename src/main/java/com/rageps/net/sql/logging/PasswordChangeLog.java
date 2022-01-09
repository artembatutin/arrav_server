package com.rageps.net.sql.logging;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.player.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by Ryley Kimmel on 11/7/2016.
 */
public final class PasswordChangeLog extends DatabaseTransaction {
	private final Player player;

	private final String oldPassword;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public PasswordChangeLog(Player player, String oldPassword) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.oldPassword = oldPassword;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO changed_passwords (session_id, username, ip_address, uid, old_password, timestamp) " + "VALUES (:session_id, :username, :ip_address, :uid, :old_password, :timestamp)")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.credentials.getHostAddress());
			statement.setString("uid", player.credentials.getUid());
			statement.setString("old_password", oldPassword);
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
