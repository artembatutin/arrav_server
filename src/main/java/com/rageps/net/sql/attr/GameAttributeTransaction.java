package com.rageps.net.sql.attr;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public final class GameAttributeTransaction extends DatabaseTransaction {

	private final GameAttribute attribute;

	private final String value;

	private final String sql;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	private GameAttributeTransaction(GameAttribute attribute, String value, String sql) {
		super(TableRepresentation.LOGGING);
		this.attribute = attribute;
		this.value = value;
		this.sql = sql;
	}

	public static GameAttributeTransaction appending(GameAttribute attribute, String append) {
		String sql = "INSERT INTO game_attributes (`key`, `value`, `timestamp`) " +
				"VALUES (:key, :value, :timestamp) ON DUPLICATE KEY UPDATE `key`=VALUES(`key`), `value`=CONCAT(`value`, ', ', VALUES(`value`)), `timestamp`=VALUES(`timestamp`);";
		return new GameAttributeTransaction(attribute, append, sql);
	}

	public static GameAttributeTransaction insertIgnore(GameAttribute attribute, String value) {
		String sql = "INSERT IGNORE INTO game_attributes (`key`, `value`, `timestamp`) VALUES (:key, :value, :timestamp);";
		return new GameAttributeTransaction(attribute, value, sql);
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, sql)) {
			statement.setString("key", attribute.name());
			statement.setString("value", value);
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
