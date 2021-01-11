package com.rageps.net.sql.punishments;

import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public final class QuerySanctionsTransaction {

	private static final String BANNED_SQL = "SELECT id, closed, expire FROM sanctions WHERE (sanction = 'ban' AND offender LIKE :username) "
			+ "OR (sanction = 'address_ban' AND (ip_address LIKE :ip_address OR offender LIKE :username)) "
			+ "OR (sanction = 'perm_ban' AND (serial_number LIKE :serial_number OR ip_address LIKE :ip_address OR offender LIKE :username))";

	private static final String MUTED_SQL = "SELECT id, closed, expire FROM sanctions WHERE (sanction = 'mute' AND offender LIKE :username) "
			+ "OR (sanction = 'address_mute' AND (ip_address LIKE :ip_address OR offender LIKE :username)) "
			+ "OR (sanction = 'perm_mute' AND (serial_number LIKE :serial_number OR ip_address LIKE :ip_address OR offender LIKE :username)) ";

	public static LoginCode execute(String username, String ipAddress, String serialNumber) throws SQLException {
		try (Connection connection = TableRepresentation.SANCTIONS.getWrapper().open()) {
			boolean banned = get(BANNED_SQL, username, ipAddress, serialNumber, connection);
			boolean muted = get(MUTED_SQL, username, ipAddress, serialNumber, connection);

			if (banned) {
				return LoginCode.ACCOUNT_DISABLED;
			}

			if (muted) {
				return LoginCode.MUTED;
			}

			return LoginCode.NORMAL;
		}
	}

	private static boolean get(String sql, String username, String ipAddress, String serialNumber, Connection connection) throws SQLException {
		boolean punished = false;

		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, sql)) {
			statement.setString("username", StringUtil.decodeUsername(username));
			statement.setString("ip_address", ipAddress);
			statement.setString("serial_number", serialNumber);

			ResultSet results = statement.executeQuery();
			while (results.next()) {
				if (results.getBoolean("closed")) {
					continue;
				}

				int id = results.getInt("id");
				Timestamp expire = new Timestamp(results.getDate("expire").getTime());

				if (LocalDateTime.now(DateTimeUtil.ZONE).isBefore(expire.toLocalDateTime())) {
					punished = true;
				} else {
					try (NamedPreparedStatement expired = NamedPreparedStatement.create(connection, "UPDATE sanctions SET closed=1 WHERE id=:id")) {
						expired.setInt("id", id);
						expired.executeUpdate();
						connection.commit();
					}
				}
			}
		}

		return punished;
	}
}
