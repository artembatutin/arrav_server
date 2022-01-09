package com.rageps.net.sql.logging;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.world.entity.actor.player.Player;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public final class WellOfGoodWillLog extends DatabaseTransaction {
	private final Player player;

	private final long amount;

	public WellOfGoodWillLog(Player player, long amount) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.amount = amount;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO wogw (session_id, name, amount) VALUES (:session_id, :name, :amount) ON DUPLICATE KEY UPDATE name=VALUES(name), amount=amount+VALUES(amount);")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("user_name", player.credentials.username);
			statement.setLong("amount", amount);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
