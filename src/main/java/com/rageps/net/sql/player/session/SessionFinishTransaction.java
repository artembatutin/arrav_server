package com.rageps.net.sql.player.session;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.world.entity.actor.player.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Finishes and finalizes a players session and updates
 * the amount of time they were logged in for.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class SessionFinishTransaction extends DatabaseTransaction {

    private static final String MYSQL_FINISH_SESSION =
            "UPDATE session_logs " +
                    "SET `finish`=NOW(), duration=? " +
                    "WHERE id=?";

    private final Player user;

    public SessionFinishTransaction(Player user) {
        super(TableRepresentation.PLAYER);
        this.user = user;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, MYSQL_FINISH_SESSION, Statement.RETURN_GENERATED_KEYS)) {
            long sessionId = user.getSession().getSessionId(); // implicit null check
            checkArgument(sessionId != -1, "session not set: %s", sessionId);
            statement.setLong(1, TimeUnit.MILLISECONDS.toSeconds(user.getSession().getSessionDuration().toMillis()));
            statement.setLong(2, sessionId);
            int affectedRows = statement.executeUpdate();
            connection.commit();
            if (affectedRows == 0) {
                throw new SQLException("Failed to finish session: " + sessionId);
            }
        }
    }

}
