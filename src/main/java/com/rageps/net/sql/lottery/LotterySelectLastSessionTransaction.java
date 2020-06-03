package com.rageps.net.sql.lottery;

import com.rageps.content.lottery.LotterySession;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LotterySelectLastSessionTransaction extends DatabaseTransactionFuture<LotterySession> {

	public LotterySelectLastSessionTransaction() {
		super(TableRepresentation.LOTTERY);
	}

	@Override
	public LotterySession onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM lottery_state WHERE id=?")) {
			statement.setInt(1, 0);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					int lastSessionId = resultSet.getInt("last_session_id");

					if (lastSessionId == -1) {
						return null;
					}
					return LotterySelectSessionTransaction.selectSession(connection, lastSessionId);
				}
			}
		}
		return null;
	}
}
