package com.rageps.net.sql.lottery;

import com.rageps.content.lottery.LotteryPlacement;
import com.rageps.content.lottery.LotterySessionWinner;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class LotteryUpdateWinnerTransaction extends DatabaseTransactionFuture<Integer> {

	private final int sessionId;

	private final Map<LotteryPlacement, LotterySessionWinner> winners;

	public LotteryUpdateWinnerTransaction(Map<LotteryPlacement, LotterySessionWinner> winners, int sessionId) {
		super(TableRepresentation.LOTTERY);
		this.sessionId = sessionId;
		this.winners = winners;
	}

	@Override
	public Integer onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement selectWinnerStatement = NamedPreparedStatement.create(connection, "SELECT * FROM lottery_entries WHERE session=?")) {
			selectWinnerStatement.setInt(1, sessionId);

			try (ResultSet resultSet = selectWinnerStatement.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("id");

					String name = resultSet.getString("username");

					Optional<LotterySessionWinner> winner = winners.values().stream().filter(w -> w.getName().equals(name)).findAny();

					if (winner.isPresent()) {
						LotterySessionWinner lotterySessionWinner = winner.get();

						String place = lotterySessionWinner.getPlacement() == LotteryPlacement.FIRST ? "first_place" :
										lotterySessionWinner.getPlacement() == LotteryPlacement.SECOND ? "second_place" : "third_place";

						try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, String.format("UPDATE lottery_sessions SET %s=? WHERE id=?", place))) {
							statement.setInt(1, id);
							statement.setInt(2, sessionId);
							statement.executeUpdate();
							connection.commit();
						}
					}
				}
				return 1;
			}
		}
	}
}
