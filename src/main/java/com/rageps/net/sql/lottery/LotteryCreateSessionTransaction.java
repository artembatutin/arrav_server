package com.rageps.net.sql.lottery;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

public class LotteryCreateSessionTransaction extends DatabaseTransactionFuture<Integer> {

	private final int ticketCost;

	private final ZonedDateTime endDate;

	public LotteryCreateSessionTransaction(int ticketCost, ZonedDateTime endDate) {
		super(TableRepresentation.LOTTERY);
		this.ticketCost = ticketCost;
		this.endDate = endDate;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 	"INSERT IGNORE INTO lottery_sessions (ticket_cost, end_date, first_place, second_place, third_place) VALUES (?, ?, ?, ?, ?)")) {
			statement.setInt(1, ticketCost);
			statement.setString(2, endDate.toString());
			statement.setInt(3, -1);
			statement.setInt(4, -1);
			statement.setInt(5, -1);
			statement.executeUpdate();
			connection.commit();

			try (NamedPreparedStatement retrieveRowStatement = NamedPreparedStatement.create(connection, "SELECT * FROM lottery_sessions WHERE end_date=?")) {
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
