package com.rageps.net.sql.daily_statistics;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-04-03 at 1:42 PM
 */
public class DailyStatisticCreateRecordTransaction extends DatabaseTransactionFuture<Boolean> {

	private final DailyStatisticCalculationTransaction calculationTransaction = new DailyStatisticCalculationTransaction();

	public DailyStatisticCreateRecordTransaction() {
		super(TableRepresentation.DAILY_STATISTICS);
	}

	@Override
	public Boolean onExecute(Connection connection) throws SQLException {
		DailyStatisticRecordRow record = calculationTransaction.onExecute(connection);

		try (PreparedStatement statement = NamedPreparedStatement.create(connection,
		 	"INSERT IGNORE INTO daily_statistical_records (timestamp, new_accounts, return_accounts) VALUES (?, ?, ?)")) {
			statement.setString(1, record.getTimestamp().toString());
			statement.setInt(2, record.getNewAccounts());
			statement.setInt(3, record.getReturnAccounts());

			statement.executeUpdate();
			connection.commit();
		}
		return true;
	}
}
