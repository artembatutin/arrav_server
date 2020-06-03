package com.rageps.net.sql.daily_statistics;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jason M on 2017-04-03 at 2:38 PM
 *
 * A transaction made to the database to determine the newest transaction.
 */
public class DailyStatisticGetAllTransaction extends DatabaseTransactionFuture<Set<DailyStatisticRecordRow>> {

	public DailyStatisticGetAllTransaction() {
		super(TableRepresentation.DAILY_STATISTICS);
	}

	@Override
	public Set<DailyStatisticRecordRow> onExecute(Connection connection) throws SQLException {
		try (PreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM daily_statistical_records;")) {
			Set<DailyStatisticRecordRow> rows = new HashSet<>();

			try (ResultSet results = statement.executeQuery()) {
				while (results.next()) {
					rows.add(new DailyStatisticRecordRow(Timestamp.valueOf(results.getString("timestamp")).toLocalDateTime().atZone(DateTimeUtil.ZONE), results.getInt("new_accounts"), results.getInt("return_accounts")));
				}
			}
			return rows;
		}
	}
}
