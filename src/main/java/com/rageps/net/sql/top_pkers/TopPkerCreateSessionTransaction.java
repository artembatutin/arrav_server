package com.rageps.net.sql.top_pkers;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Created by Jason M on 2017-03-13 at 2:09 PM
 */
public class TopPkerCreateSessionTransaction extends DatabaseTransactionFuture<Integer> {

	/**
	 * The end date of the new session.
	 */
	private final ZonedDateTime endDate;

	public TopPkerCreateSessionTransaction(ZonedDateTime endDate) {
		super(TableRepresentation.TOP_PKER);
		this.endDate = endDate;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 	"INSERT IGNORE INTO top_pker_sessions (winner, end_date) VALUES (?, ?)")) {
			statement.setInt(1, -1);
			statement.setString(2, endDate.toString());
			statement.executeUpdate();
			connection.commit();

			try (NamedPreparedStatement retrieveRowStatement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_sessions WHERE end_date=?")) {
				retrieveRowStatement.setString(1, endDate.toString());

				try (ResultSet results = retrieveRowStatement.executeQuery()) {
					while (results.next()) {
						return results.getInt("id");
					}
				}
			}
		}
		return -1;
	}
}
