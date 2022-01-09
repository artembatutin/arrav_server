package com.rageps.net.sql.top_pkers;

import com.rageps.content.top_pker.Winner;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-03-13 at 2:57 PM
 */
public class TopPkerUpdateWinnerTransaction extends DatabaseTransactionFuture<Integer> {

	private final int sessionId;

	private final Winner winner;

	public TopPkerUpdateWinnerTransaction(Winner winner, int sessionId) {
		super(TableRepresentation.TOP_PKER);
		this.sessionId = sessionId;
		this.winner = winner;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement selectWinnerStatement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_entries WHERE session=? AND username=?")) {
			selectWinnerStatement.setInt(1, sessionId);
			selectWinnerStatement.setString(2, StringUtil.longToString(winner.getUsernameAsLong()));

			try (ResultSet resultSet = selectWinnerStatement.executeQuery()) {
				while (resultSet.next()) {
					int winnerRowId = resultSet.getInt("id");

					try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "UPDATE top_pker_sessions SET winner=? WHERE id=?")) {
						statement.setInt(1, winnerRowId);
						statement.setInt(2, sessionId);
						statement.executeUpdate();
						connection.commit();
						return 1;
					}
				}
			}
		}
		return 0;
	}
}
