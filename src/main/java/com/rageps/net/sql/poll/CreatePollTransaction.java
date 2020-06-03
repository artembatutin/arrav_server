package com.rageps.net.sql.poll;

import com.rageps.content.poll.Poll;
import com.rageps.content.poll.PollOrder;
import com.rageps.content.poll.PollResult;
import com.rageps.content.poll.PollState;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Created by Jason MacKeigan on 2017-06-06 at 11:40 PM
 */
public class CreatePollTransaction extends DatabaseTransactionFuture<Poll> {

	private final PollOrder order;

	private final String question;

	private final String description;

	private final String optionOne;

	private final String optionTwo;

	private final String optionThree;

	private final String optionFour;

	private final int daysUntilEnd;

	public CreatePollTransaction(PollOrder order, String question, String description, String optionOne, String optionTwo, String optionThree, String optionFour, int daysUntilEnd) {
		super(TableRepresentation.VOTE_POLL);
		this.order = order;
		this.question = question;
		this.description = description;
		this.optionOne = optionOne;
		this.optionTwo = optionTwo;
		this.optionThree = optionThree;
		this.optionFour = optionFour;
		this.daysUntilEnd = daysUntilEnd;
	}

	@Override
	public Poll onExecute(Connection connection) throws SQLException {
		ZonedDateTime endDate = ZonedDateTime.now(DateTimeUtil.ZONE).plusDays(daysUntilEnd).withHour(12).withMinute(0).withSecond(0);

		try (NamedPreparedStatement insertStatement = NamedPreparedStatement.create(connection,
		 		"INSERT INTO poll (end_date, question, description, option_one, option_two, option_three, option_four, placement) VALUES(?, ?, ?, ?, ?, ?, ?, ?);",
		 		PreparedStatement.RETURN_GENERATED_KEYS)) {
			insertStatement.setString(1, endDate.toString());
			insertStatement.setString(2, question);
			insertStatement.setString(3, description);
			insertStatement.setString(4, optionOne);
			insertStatement.setString(5, optionTwo);
			insertStatement.setString(6, optionThree);
			insertStatement.setString(7, optionFour);
			insertStatement.setString(8, order.name());

			insertStatement.executeUpdate();
			try (ResultSet insertResult = insertStatement.getGeneratedKeys()) {
				connection.commit();

				int id = -1;

				while (insertResult.next()) {
					id = insertResult.getInt(1);
				}
				if (id != -1) {
					return new Poll(id, endDate, question, description, optionOne, optionTwo, optionThree, optionFour, PollState.OPEN, PollResult.TO_BE_DETERMINED, null, order);
				}
			}
		}
		return null;
	}
}
