package com.rageps.net.sql.top_pkers;

import com.rageps.content.top_pker.OnDemandReward;
import com.rageps.content.top_pker.PredefinedReward;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jason M on 2017-03-15 at 5:13 PM
 */
public class TopPkerGenerateRewardsTransaction extends DatabaseTransactionFuture<Set<OnDemandReward>> {

	/**
	 * The session that we're obtaining the rewards for.
	 */
	private final int session;

	public TopPkerGenerateRewardsTransaction(int session) {
		super(TableRepresentation.TOP_PKER);
		this.session = session;
	}

	@Override
	public Set<OnDemandReward> onExecute(Connection connection) throws SQLException {
		Set<OnDemandReward> rewards = new HashSet<>();

		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM top_pker_rewards WHERE session_id=?")) {
			statement.setInt(1, session);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					PredefinedReward predefinedReward = PredefinedReward.valueOf(resultSet.getString("predefined_reward"));

					if (predefinedReward == PredefinedReward.NONE) {
						int itemId = resultSet.getInt("item_id");

						int itemAmount = Math.max(1, resultSet.getInt("item_amount"));

						if (itemId > -1 && itemId > 0) {
							rewards.add(new OnDemandReward(session, itemId, itemAmount));
						}
					} else {
						rewards.add(new OnDemandReward(session, predefinedReward));
					}
				}
			}
		}
		return rewards;
	}
}
