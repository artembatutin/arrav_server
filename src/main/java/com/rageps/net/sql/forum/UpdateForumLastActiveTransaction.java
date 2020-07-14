package com.rageps.net.sql.forum;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.world.entity.actor.player.PlayerCredentials;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class UpdateForumLastActiveTransaction extends DatabaseTransaction {

    private final String MYSQL_FORUM_UPDATE_LAST_ACTIVE = "UPDATE core_members set last_visit=?, last_activity=? WHERE member_id=?";

    private final PlayerCredentials credentials;

    public UpdateForumLastActiveTransaction(PlayerCredentials credentials) {
        super(TableRepresentation.FORUM);
        this.credentials = credentials;
    }

    @Override
    public void execute(Connection connection) throws SQLException {

        try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, MYSQL_FORUM_UPDATE_LAST_ACTIVE)) {
            statement.setLong(1, System.currentTimeMillis() / 1000);
            statement.setLong(2, System.currentTimeMillis() / 1000);
            statement.setLong(3, credentials.databaseId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to log last active for player: " + credentials.username);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

}
