package com.rageps.net.sql.poll;

import com.rageps.content.poll.*;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:59 PM
 */
public class SelectPollsTransaction extends DatabaseTransactionFuture<List<Poll>> {

	public SelectPollsTransaction() {
		super(TableRepresentation.VOTE_POLL);
	}

	@Override
	public List<Poll> onExecute(Connection connection) throws SQLException {
		List<Integer> pollIds = new ArrayList<>();

		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM polls WHERE id=0")) {
			try (ResultSet results = statement.executeQuery()) {
				while (results.next()) {
					List<Integer> polls = Arrays.asList(results.getInt("first"), results.getInt("second"), results.getInt("third"),
					 	results.getInt("fourth"), results.getInt("fifth"));

					polls.stream().filter(poll -> poll != -1).forEach(pollIds::add);
				}
			}
		}
		List<Poll> polls = new ArrayList<>();

		for (Integer pollId : pollIds) {
			try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM poll WHERE id=?")) {
				statement.setInt(1, pollId);

				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						String optionIfSuccessful = results.getString("option_if_successful");

						Poll poll = new Poll(results.getInt("id"), ZonedDateTime.parse(results.getString("end_date")),
										   results.getString("question"),
										   results.getString("description"),
										   results.getString("option_one"), results.getString("option_two"),
										   results.getString("option_three"),
										   results.getString("option_four"),
										   PollState.valueOf(results.getString("state")),
										   PollResult.valueOf(results.getString("result_if_any")),
										   optionIfSuccessful == null ? null : PollOption.valueOf(optionIfSuccessful),
											 PollOrder.valueOf(results.getString("placement")));

						try (NamedPreparedStatement voterEntriesStatement = NamedPreparedStatement.create(connection, "SELECT * FROM poll_votes WHERE poll_id=?")) {
							voterEntriesStatement.setInt(1, pollId);

							try (ResultSet voterResults = voterEntriesStatement.executeQuery()) {
								while (voterResults.next()) {
									poll.addVote(new PollVote(voterResults.getInt("poll_id"),
															  voterResults.getString("id_username_hash"), voterResults.getString("username"),
															  PollOption.valueOf(voterResults.getString("vote_option"))));
								}
							}
						}
						polls.add(poll);
					}
				}
			}
		}
		return polls;
	}

}
