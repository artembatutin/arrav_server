package com.rageps.net.sql.lottery;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.rageps.content.lottery.LotterySessionEntry;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

public class LotteryUpdateEntryTransaction extends DatabaseTransactionFuture<Integer> {

	private final int session;

	private final LotterySessionEntry entry;

	public LotteryUpdateEntryTransaction(int session, LotterySessionEntry entry) {
		super(TableRepresentation.LOTTERY);
		this.session = session;
		this.entry = entry;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 		"INSERT IGNORE INTO lottery_entries (hash, username, session, tickets) VALUES (?, ?, ?, ?) "
				 + "ON DUPLICATE KEY UPDATE tickets=VALUES(tickets);")) {
			statement.setLong(1, Hashing.md5().newHasher().putString(entry.getName(), Charsets.UTF_8).putInt(session).hash().asLong());
			statement.setString(2, entry.getName());
			statement.setInt(3, session);
			statement.setInt(4, entry.getTickets());
			statement.executeUpdate();
			connection.commit();
			return 1;
		}
	}
}
