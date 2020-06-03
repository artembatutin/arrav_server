package com.rageps.net.sql.punishments;

import com.rageps.content.moderation.Punishment;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Submits a {@link Punishment} to the database.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public final class UpdatePunishmentTransaction extends DatabaseTransaction {

	private final Punishment punishment;

	public UpdatePunishmentTransaction(Punishment punishment) {
		super(TableRepresentation.SANCTIONS);
		this.punishment = punishment;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO punishment (`user_name`,`agent`, `session_id`,`expireDate`,`duration`,`macAddress`,`hostAddress`,`uid`,`punishmentPolicy`,`reason`,`punishmentType`) "
			+ "VALUES (:user_name, :agent, :session_id, :expireDate, :duration, :macAddress, :hostAddress, :uid, :punishmentPolicy, :reason, :punishmentType );")) {
			statement.setString("user_name", punishment.getOffender());
			statement.setString("agent", punishment.getAgent());
			statement.setLong("session_id", punishment.getOffenderSession());
			statement.setLong("expireDate", punishment.getExpireDate());
			statement.setString("duration", punishment.getDuration());
			statement.setString("macAddress", punishment.getMac());
			statement.setString("hostAddress", punishment.getHost());
			statement.setString("uid", punishment.getUid());
			statement.setString("punishmentPolicy", punishment.getPunishmentPolicy().name());
			statement.setString("reason", punishment.getReason());
			statement.setString("punishmentType", punishment.getPunishmentType().name());
			statement.executeUpdate();
			connection.commit();
		}
	}

}
