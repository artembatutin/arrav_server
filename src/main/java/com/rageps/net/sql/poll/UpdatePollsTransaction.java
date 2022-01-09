package com.rageps.net.sql.poll;

import com.rageps.content.poll.Poll;
import com.rageps.content.poll.PollOrder;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Jason MacKeigan on 2017-06-07 at 11:52 AM
 */
public class UpdatePollsTransaction extends DatabaseTransactionFuture {

	private final Map<PollOrder, Poll> polls;

	public UpdatePollsTransaction(Map<PollOrder, Poll> polls) {
		super(TableRepresentation.VOTE_POLL);
		this.polls = polls;
	}

	@Override
	public Object onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "UPDATE polls SET first=?, second=?, third=?, fourth=?, fifth=? WHERE id=0")) {
			statement.setInt(1, polls.containsKey(PollOrder.FIRST) ? polls.get(PollOrder.FIRST).getId() : -1);
			statement.setInt(2, polls.containsKey(PollOrder.SECOND) ? polls.get(PollOrder.SECOND).getId() : -1);
			statement.setInt(3, polls.containsKey(PollOrder.THIRD) ? polls.get(PollOrder.THIRD).getId() : -1);
			statement.setInt(4, polls.containsKey(PollOrder.FOURTH) ? polls.get(PollOrder.FOURTH).getId() : -1);
			statement.setInt(5, polls.containsKey(PollOrder.FIFTH) ? polls.get(PollOrder.FIFTH).getId() : -1);
			statement.executeUpdate();
			connection.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
