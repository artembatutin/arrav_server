package com.rageps.net.sql.poll;

import com.rageps.content.poll.PollVote;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 7:12 PM
 */
public class CreateAllVotesTransaction extends DatabaseTransactionFuture {

	private final Collection<PollVote> votes;

	public CreateAllVotesTransaction(Collection<PollVote> votes) {
		super(TableRepresentation.VOTE_POLL);
		this.votes = votes;
	}

	@Override
	public Object onExecute(Connection connection) throws SQLException {
		for (PollVote vote : votes) {
			CreateVoteTransaction transaction = new CreateVoteTransaction(vote);

			transaction.onExecute(connection);
		}
		return null;
	}
}
