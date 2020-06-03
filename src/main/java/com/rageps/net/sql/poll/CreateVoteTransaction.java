package com.rageps.net.sql.poll;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.rageps.content.poll.PollVote;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 6:57 PM
 */
public class CreateVoteTransaction extends DatabaseTransactionFuture {

	private final PollVote vote;

	public CreateVoteTransaction(PollVote vote) {
		super(TableRepresentation.VOTE_POLL);
		this.vote = vote;
	}

	@Override
	public Object onExecute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT IGNORE INTO poll_votes (poll_id, id_username_hash, username, vote_option)"
																						   + "VALUES (?, ?, ?, ?)")) {
			statement.setInt(1, vote.getPollId());
			statement.setLong(2, Hashing.md5().newHasher().putInt(vote.getPollId()).putString(vote.getUsername(), Charsets.UTF_8).hash().asLong());
			statement.setString(3, vote.getUsername());
			statement.setString(4, vote.getOption().name());
			statement.executeUpdate();
			connection.commit();
		}
		return null;
	}
}
