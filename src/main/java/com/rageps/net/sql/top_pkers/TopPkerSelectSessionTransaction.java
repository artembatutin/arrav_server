package com.rageps.net.sql.top_pkers;

import com.rageps.content.top_pker.TopPkerSession;
import com.rageps.content.top_pker.Winner;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.util.StringUtil;

import java.sql.*;

/**
 * Created by Jason M on 2017-03-13 at 1:24 PM
 *
 * Execute function will retrieve the
 */
public class TopPkerSelectSessionTransaction extends DatabaseTransactionFuture<TopPkerSession> {

	public TopPkerSelectSessionTransaction() {
		super(TableRepresentation.TOP_PKER);
	}

	@Override
	public TopPkerSession onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_state WHERE id=?")) {
			statement.setInt(1, 1);
			try (ResultSet results = statement.executeQuery()) {

				if (results.next()) {
					int session = results.getInt("session");

					return selectSession(connection, session);
				}
			}
		}
		return null;
	}

	public static TopPkerSession selectSession(Connection connection, int session) throws SQLException {
		try (NamedPreparedStatement sessionStatement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_sessions WHERE id=?")) {
			sessionStatement.setInt(1, session);

			try (ResultSet sessionResults = sessionStatement.executeQuery()) {
				if (sessionResults.next()) {
					int winnerEntriesId = sessionResults.getInt("winner");

					TopPkerSession currentSession = new TopPkerSession(session, Timestamp.valueOf(sessionResults.getString("end_date")).toLocalDateTime().atZone(DateTimeUtil.ZONE));

					try (PreparedStatement entryStatement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_entries WHERE session=?")) {
						entryStatement.setInt(1, session);

						try (ResultSet entryResultSet = entryStatement.executeQuery()) {
							while (entryResultSet.next()) {
								int entryId = entryResultSet.getInt("id");

								long usernameAsLong = StringUtil.stringToLong(entryResultSet.getString("username"));

								int kills = entryResultSet.getInt("kills");

								int deaths = entryResultSet.getInt("deaths");

								currentSession.putOrAdd(usernameAsLong, kills, deaths);

								if (entryId == winnerEntriesId) {
									currentSession.setWinnerIfAbsent(new Winner(usernameAsLong, kills, deaths));
								}
							}
						}
					}
					return currentSession;
				}
			}
		}
		return null;
	}
}
