package com.rageps.net.sql.starter;

import com.rageps.net.sql.TableRepresentation;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.assets.group.GameMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class StarterQueryTransaction {
    private static final String QUERY_SQL = "SELECT game_mode, uid FROM received_starters;";

    public static Map<String, Map<GameMode, Integer>> init() throws SQLException {
        // <uid, <game_mode, count>>
        Map<String, Map<GameMode, Integer>> receivedStarters = new HashMap<>();
        if (!World.get().getEnvironment().isSqlEnabled()) {
            return receivedStarters;
        }

        try (Connection connection = TableRepresentation.LOGGING.getWrapper().open();
             PreparedStatement statement = connection.prepareStatement(QUERY_SQL)) {
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String uid = results.getString("uid");
                GameMode gameMode = GameMode.valueOf(results.getString("game_mode"));

                Map<GameMode, Integer> counts = receivedStarters.computeIfAbsent(uid, __ -> new HashMap<>());
                counts.merge(gameMode, 1, Integer::sum);
            }
        }

        return receivedStarters;
    }

}
