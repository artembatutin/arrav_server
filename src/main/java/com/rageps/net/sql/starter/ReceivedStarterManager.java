package com.rageps.net.sql.starter;

import com.google.common.collect.ImmutableMap;
import com.rageps.net.sql.UncheckedSQLException;
import com.rageps.world.GameMode;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class ReceivedStarterManager {
    private static final Map<GameMode, Integer> THRESHOLD = ImmutableMap.of(
            GameMode.NORMAL, 3,
            GameMode.PK_MODE, 1
    );

    private static final Map<String, Map<GameMode, Integer>> receivedStarters = new HashMap<>();

    static {
        try {
            receivedStarters.putAll(StarterQueryTransaction.init());
        } catch (SQLException cause) {
            throw new UncheckedSQLException("Unable to initialize received starters!", cause);
        }
    }

    public static boolean thresholdReached(Player player) {
        GameMode gameMode = player.getGameMode();
        int threshold = THRESHOLD.getOrDefault(gameMode, -1);
        if (threshold == -1) {
            return false;
        }

        Map<GameMode, Integer> counts = receivedStarters.computeIfAbsent(player.getSession().getUid(), __ -> new HashMap<>());

        if (counts.getOrDefault(gameMode, 0) < threshold) {
            counts.merge(gameMode, 1, Integer::sum);
            World.get().getDatabaseWorker().submit(new StarterInsertTransaction(player));
            return false;
        }

        return true;
    }
}
