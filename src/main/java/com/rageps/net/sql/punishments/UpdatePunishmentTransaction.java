package com.rageps.net.sql.punishments;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Submits a {@link Sanction} to the database.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public final class UpdatePunishmentTransaction extends DatabaseTransaction {

	private final String moderator;

	private final Sanction sanction;

	private final String offender;

	private final String ipAddress;

	private final String serialNumber;

	private final Timestamp expire;

	private final String reason;

	private final long sessionId;

	public UpdatePunishmentTransaction(String moderator, Sanction sanction, String offender, long sessionId, String ipAddress, String serialNumber, Timestamp expire, String reason) {
		super(TableRepresentation.SANCTIONS);
		this.moderator = moderator;
		this.sessionId = sessionId;
		this.sanction = sanction;
		this.offender = offender;
		this.ipAddress = ipAddress;
		this.serialNumber = serialNumber;
		this.expire = expire;
		this.reason = reason;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
				"INSERT INTO sanctions (sanction, moderator, offender, session_id, ip_address, serial_number, expire, reason) "
						+ "VALUES (:sanction, :moderator, :offender, :session_id, :ip_address, :serial_number, :expire, :reason);")) {
			statement.setString("sanction", sanction.name().toLowerCase());
			statement.setString("moderator", moderator);
			statement.setString("offender", offender);
			statement.setLong("session_id", sessionId);
			statement.setString("ip_address", ipAddress);
			statement.setString("serial_number", serialNumber);
			statement.setTimestamp("expire", expire);
			statement.setString("reason", reason);
			statement.executeUpdate();
			connection.commit();
		}
	}

}
