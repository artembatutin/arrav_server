package com.rageps.net.sql.daily_statistics;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Created by Jason M on 2017-04-03 at 11:56 AM
 */
public class DailyStatisticInsertTransaction extends DatabaseTransaction {

	/**
	 * The username of the player.
	 */
	private final String username;

	/**
	 * The host address of the player.
	 */
	private final String hostAddress;

	/**
	 * The flag that determines if the account is new or not.
	 */
	private final boolean newAccount;

	public DailyStatisticInsertTransaction(String username, String hostAddress, boolean newAccount) {
		super(TableRepresentation.DAILY_STATISTICS);
		this.username = username;
		this.hostAddress = hostAddress;
		this.newAccount = newAccount;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (PreparedStatement statement = NamedPreparedStatement.create(connection,
		 		"INSERT IGNORE INTO daily_statistical_entries (username, host_address, timestamp, new_account) VALUES (?, ?, ?, ?);")) {
			statement.setString(1, username);
			statement.setString(2, hostAddress);
			statement.setString(3, ZonedDateTime.now(DateTimeUtil.ZONE).toString());
			statement.setString(4, Boolean.toString(newAccount));

			statement.executeUpdate();
			connection.commit();
		}
	}
}
