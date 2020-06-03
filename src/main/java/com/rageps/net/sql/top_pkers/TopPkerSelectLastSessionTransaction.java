package com.rageps.net.sql.top_pkers;

import com.rageps.content.top_pker.TopPkerSession;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-03-15 at 4:15 AM
 */
public class TopPkerSelectLastSessionTransaction extends DatabaseTransactionFuture<TopPkerSession> {

	public TopPkerSelectLastSessionTransaction() {
		super(TableRepresentation.TOP_PKER);
	}

	@Override
	public TopPkerSession onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_state WHERE id=?")) {
			statement.setInt(1, 1);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					int lastSessionId = resultSet.getInt("last_session");

					if (lastSessionId == -1) {
						return null;
					}

					return TopPkerSelectSessionTransaction.selectSession(connection, lastSessionId);
				}
			}
		}
		return null;
	}
}
