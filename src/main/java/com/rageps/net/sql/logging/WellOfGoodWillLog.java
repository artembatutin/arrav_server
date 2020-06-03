package com.rageps.net.sql.logging;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public final class WellOfGoodWillLog extends DatabaseTransaction {
	private final String name;

	private final long amount;

	public WellOfGoodWillLog(String name, long amount) {
		super(TableRepresentation.LOGGING);
		this.name = name;
		this.amount = amount;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO wogw (name, amount) VALUES (:name, :amount) ON DUPLICATE KEY UPDATE name=VALUES(name), amount=amount+VALUES(amount);")) {
			statement.setString("name", name);
			statement.setLong("amount", amount);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
