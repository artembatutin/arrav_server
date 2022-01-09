package com.rageps.net.sql.daily_statistics;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jason M on 2017-04-03 at 3:12 PM
 */
public class DailyStatisticTruncateTransaction extends DatabaseTransactionFuture<Boolean> {

	public DailyStatisticTruncateTransaction() {
		super(TableRepresentation.DAILY_STATISTICS);
	}

	@Override
	public Boolean onExecute(Connection connection) throws SQLException {
		try (PreparedStatement truncateStatement = NamedPreparedStatement.create(connection, "TRUNCATE TABLE daily_statistical_entries;")) {
			truncateStatement.execute();
		}
		return true;
	}
}
