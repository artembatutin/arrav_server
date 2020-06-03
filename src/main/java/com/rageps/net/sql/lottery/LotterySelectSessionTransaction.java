package com.rageps.net.sql.lottery;

import com.rageps.content.lottery.LotteryPlacement;
import com.rageps.content.lottery.LotterySession;
import com.rageps.content.lottery.LotterySessionWinner;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LotterySelectSessionTransaction extends DatabaseTransactionFuture<LotterySession> {

	public LotterySelectSessionTransaction() {
		super(TableRepresentation.LOTTERY);
	}

	@Override
	public LotterySession onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM lottery_state WHERE id=?")) {
			statement.setInt(1, 0);
			try (ResultSet results = statement.executeQuery()) {

				if (results.next()) {
					int session = results.getInt("session_id");

					return selectSession(connection, session);
				}
			}
		}
		return null;
	}

	public static LotterySession selectSession(Connection connection, int session) throws SQLException {
		try (NamedPreparedStatement sessionStatement = NamedPreparedStatement.create(connection, "SELECT * FROM lottery_sessions WHERE id=?")) {
			sessionStatement.setInt(1, session);

			try (ResultSet sessionResults = sessionStatement.executeQuery()) {

				if (sessionResults.next()) {
					int ticketCost = sessionResults.getInt("ticket_cost");

					int firstPlace = sessionResults.getInt("first_place");

					int secondPlace = sessionResults.getInt("second_place");

					int thirdPlace = sessionResults.getInt("third_place");

					Map<LotteryPlacement, LotterySessionWinner> winners = new HashMap<>();

					LotterySession currentSession = new LotterySession(session,
																	   Timestamp.valueOf(sessionResults.getString("end_date")).toLocalDateTime().atZone(DateTimeUtil.ZONE),
																	   ticketCost);

					try (PreparedStatement entryStatement = NamedPreparedStatement.create(connection, "SELECT * FROM lottery_entries WHERE session=?")) {
						entryStatement.setInt(1, session);

						try (ResultSet entryResultSet = entryStatement.executeQuery()) {
							while (entryResultSet.next()) {
								int entryId = entryResultSet.getInt("id");

								String username = entryResultSet.getString("username");

								int tickets = entryResultSet.getInt("tickets");

								currentSession.putOrAdd(username, tickets);

								if (entryId == firstPlace) {
									winners.put(LotteryPlacement.FIRST, new LotterySessionWinner(LotteryPlacement.FIRST, username, tickets));
								} else if (entryId == secondPlace) {
									winners.put(LotteryPlacement.SECOND, new LotterySessionWinner(LotteryPlacement.SECOND, username, tickets));
								} else if (entryId == thirdPlace) {
									winners.put(LotteryPlacement.THIRD, new LotterySessionWinner(LotteryPlacement.THIRD, username, tickets));
								}
							}
						}
						if (!winners.isEmpty()) {
							currentSession.setWinnersIfAbsent(winners);
						}
					}
					return currentSession;
				}
			}
		}
		return null;
	}
}
