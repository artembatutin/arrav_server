package com.rageps.net.sql.starter;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.player.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public final class StarterInsertTransaction extends DatabaseTransaction {
    private static final String INSERT_SQL = "INSERT INTO received_starters (username, game_mode, ip_address, uid, timestamp) VALUES (:username, :game_mode, :ip_address, :uid, :timestamp);";

    private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));
    private final Player player;

    public StarterInsertTransaction(Player player) {
        super(TableRepresentation.LOGGING);
        this.player = player;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, INSERT_SQL)) {
            statement.setString("username", player.credentials.username);
            statement.setString("game_mode", player.getGameMode().name());
            statement.setString("ip_address", player.credentials.getHostAddress());
            statement.setString("uid", player.credentials.getUid());
            statement.setTimestamp("timestamp", timestamp);
            statement.execute();
            connection.commit();
        }
    }
}
