package com.rageps.net.sql.daily_statistics;

import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jason M on 2017-04-03 at 1:08 PM
 */
public class DailyStatisticCalculationTransaction extends DatabaseTransactionFuture<DailyStatisticRecordRow> {

	public DailyStatisticCalculationTransaction() {
		super(TableRepresentation.DAILY_STATISTICS);
	}

	@Override
	public DailyStatisticRecordRow onExecute(Connection connection) throws SQLException {
		Set<DailyStatisticCurrentRow> rows = new HashSet<>(256);

		try (PreparedStatement statement = NamedPreparedStatement.create(connection, "SELECT * FROM daily_statistical_entries")) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					rows.add(new DailyStatisticCurrentRow(resultSet.getString("username"),
														  Timestamp.valueOf(resultSet.getString("timestamp")).toLocalDateTime().atZone(DateTimeUtil.ZONE), Boolean.parseBoolean(resultSet.getString("new_account"))));
				}
			}
		}
		return new DailyStatisticRecordRow(ZonedDateTime.now(DateTimeUtil.ZONE), (int) rows.stream().filter(DailyStatisticCurrentRow::isNewAccount).count(),
												(int) rows.stream().filter(row -> !row.isNewAccount()).count());
	}
}
