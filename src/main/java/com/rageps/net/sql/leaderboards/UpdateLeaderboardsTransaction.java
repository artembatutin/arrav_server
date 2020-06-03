package com.rageps.net.sql.leaderboards;

import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.GameMode;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.env.Environment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryley Kimmel on 10/17/2016.
 */
public final class UpdateLeaderboardsTransaction extends DatabaseTransaction {

    private final Player player;

    public UpdateLeaderboardsTransaction(Player player) {
        super(TableRepresentation.LEADERBOARDS);
        this.player = player;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        if (!World.get().getEnvironment().isSqlEnabled()) {
            return;
        }
        if (World.get().getEnvironment().getType() != Environment.Type.LIVE) {
            return;
        }
        if (player.getRights().equals(Rights.ADMINISTRATOR)) {
            return;
        }

        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        if (!player.getGameMode().equals(GameMode.PK_MODE)) {
            Map<Integer, LeaderboardEntry> leaderboards = new HashMap<>();

            try (NamedPreparedStatement select = NamedPreparedStatement.create(connection, "SELECT skill, experience, last_updated FROM overall_skill WHERE username LIKE :username;");
                 NamedPreparedStatement delete = NamedPreparedStatement.create(connection, "DELETE FROM overall_skill WHERE username LIKE :username;")) {
                select.setString("username", player.credentials.username);
                delete.setString("username", player.credentials.username);

                try (ResultSet results = select.executeQuery()) {
                    while (results.next()) {
                        int skill = results.getInt("skill");
                        int experience = results.getInt("experience");
                        Timestamp timestamp = results.getTimestamp("last_updated");
                        leaderboards.put(skill, new LeaderboardEntry(skill, experience, timestamp));
                    }
                }

                delete.executeUpdate();
            }

            try (NamedPreparedStatement overallSkillInsert = NamedPreparedStatement.create(connection, "INSERT INTO overall_skill (username, last_updated, skill, level, experience) VALUES (:username, :last_updated, :skill, :level, :experience);");
                 NamedPreparedStatement overallInsert = NamedPreparedStatement.create(connection, "INSERT INTO overall (username, total_level, total_experience, game_mode, experience_rate) VALUES (:username, :total_level, :total_experience, :game_mode, :experience_rate)" +
                         "ON DUPLICATE KEY UPDATE username=VALUES(username), total_level=VALUES(total_level), total_experience=VALUES(total_experience), game_mode=VALUES(game_mode), experience_rate=VALUES(experience_rate);")) {

                overallInsert.setString("username", player.credentials.username);
                overallInsert.setInt("total_level", Skills.getTotalLevel(player));
                overallInsert.setLong("total_experience", Skills.getTotalExp(player));
                overallInsert.setString("game_mode", player.getGameMode().name());
                overallInsert.setString("experience_rate", player.getExperienceRate().name());

                Timestamp now = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));
                for(int i = 0; i < player.getSkills().length; i++) {
                    Skill skill = player.getSkills()[i];
                    int experience = (int) skill.getExperience();
                    int level = skill.getLevelForExperience();
                    int minimum = i == Skills.HITPOINTS ? 10 : 1;

                    // Don't include skills that are level 1, save bandwidth br0
                    if (level == minimum) {
                        continue;
                    }

                    overallSkillInsert.setString("username", player.credentials.username);
                    overallSkillInsert.setTimestamp("last_updated", now);

                    // Deal with special cases, a player with 200m experience will not have their 'last_updated' changed as to not lose their position on the leaderboard
                    LeaderboardEntry entry = leaderboards.get(i);
                    if (entry != null) {
                        int oldExperience = entry.getExperience();
                        if (oldExperience >= 200_000_000 || oldExperience == experience) {
                            overallSkillInsert.setTimestamp("last_updated", entry.getTimestamp());
                        }
                    }

                    overallSkillInsert.setInt("skill", i);
                    overallSkillInsert.setInt("level", level);
                    overallSkillInsert.setInt("experience", experience);
                    overallSkillInsert.addBatch();
                    overallSkillInsert.clearParameters();
                }

                overallSkillInsert.executeBatch();
                overallInsert.execute();
                connection.setAutoCommit(autoCommit);
            }
        }

        /*int kills = player.getAttributeMap().getInt(PkRewards.KILLS), deaths = player.getAttributeMap().getInt(PkRewards.DEATHS);
        int currKillstreak = player.getAttributeMap().getInt(PkRewards.KILLSTREAK), longestKillstreak = player.getAttributeMap().getInt(PkRewards.HIGHEST_KILLSTREAK);
        if (kills > 0) {
            try (NamedPreparedStatement insert = NamedPreparedStatement.create(connection, "INSERT INTO wilderness (username, kills, deaths, current_killstreak, highest_killstreak) VALUES (:username, :kills, :deaths, :current_killstreak, :highest_killstreak)" +
                    "ON DUPLICATE KEY UPDATE username=VALUES(username), kills=VALUES(kills), deaths=VALUES(deaths), current_killstreak=VALUES(current_killstreak), highest_killstreak=VALUES(highest_killstreak);")) {
                insert.setString("username", player.credentials.username);
                insert.setInt("kills", kills);
                insert.setInt("deaths", deaths);
                insert.setInt("current_killstreak", currKillstreak);
                insert.setInt("highest_killstreak", longestKillstreak);
                insert.execute();
            }
        }*/

        connection.commit();
    }


    private static final class LeaderboardEntry {
        private final int skill;
        private final int experience;
        private final Timestamp timestamp;

        public LeaderboardEntry(int skill, int experience, Timestamp timestamp) {
            this.skill = skill;
            this.experience = experience;
            this.timestamp = timestamp;
        }

        public int getExperience() {
            return experience;
        }

        public int getSkill() {
            return skill;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }
    }

}
