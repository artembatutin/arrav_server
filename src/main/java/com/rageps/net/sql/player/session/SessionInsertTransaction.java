package com.rageps.net.sql.player.session;

import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.player.Player;

import java.sql.*;
import java.time.Instant;

/**
 * Creates and commits a session id, and other details
 * related with their session.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class SessionInsertTransaction extends DatabaseTransactionFuture<Long> {

    private static final String CREATE_SESSION =
            "INSERT INTO session_logs (user_id, `name`, ip, mac, `serial`, response) " +
                    "VALUES (?,?,?,?,?,?)";

    private final Player user;

    private final LoginCode code;

    public SessionInsertTransaction(Player user, LoginCode code) {
        super(TableRepresentation.PLAYER);
        this.user = user;
        this.code = code;
    }

    @Override
    public Long onExecute(Connection connection) throws SQLException {
        try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, CREATE_SESSION, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, user.credentials.databaseId);
            statement.setString(2, user.credentials.username);
            statement.setString(3, user.credentials.getHostAddress());
            statement.setString(4, user.credentials.getMacAddress());
            statement.setString(5, user.credentials.getUid());
            statement.setInt(6, code.getCode());
            statement.executeUpdate();
            connection.commit();

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to insert session: " + user.credentials.username+" "+user.credentials.getHostAddress()+" response: "+code.name());
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
            throw new SQLException("No keys generated for session ["
                    + user + "] from " + user.credentials.getHostAddress() + " [response: " + code.getCode() + "]");
        }
    }
}
