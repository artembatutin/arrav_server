package com.rageps.net.sql.poll;

import com.rageps.content.poll.Poll;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 6:38 PM
 */
public class UpdatePollTransaction extends DatabaseTransactionFuture {

	private final Poll poll;

	public UpdatePollTransaction(Poll poll) {
		super(TableRepresentation.VOTE_POLL);
		this.poll = poll;
	}

	@Override
	public Object onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 			"INSERT INTO poll (id, end_date, question, description, option_one, option_two, option_three, option_four, state, result_if_any, option_if_successful, placement)"
					 + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE end_date=VALUES(end_date), description=VALUES(description),"
					 + " option_one=VALUES(option_one), option_two=VALUES(option_two), option_three=VALUES(option_three), option_four=VALUES(option_four), state=VALUES(state),"
					 + " result_if_any=VALUES(result_if_any), option_if_successful=VALUES(option_if_successful), placement=VALUES(placement)")) {
			statement.setInt(1, poll.getId());
			statement.setString(2, poll.getEndDate().toString());
			statement.setString(3, poll.getQuestion());
			statement.setString(4, poll.getDescription());
			statement.setString(5, poll.getOptionOne());
			statement.setString(6, poll.getOptionTwo());
			statement.setString(7, poll.getOptionThree());
			statement.setString(8, poll.getOptionFour());
			statement.setString(9, poll.getState().name());
			statement.setString(10, poll.getResult().name());
			statement.setString(11, !poll.getOptionIfSuccessful().isPresent() ? null : poll.getOptionIfSuccessful().get().name());
			statement.setString(12, poll.getOrder().name());
			statement.executeUpdate();
			connection.commit();
		}
		return null;
	}
}
