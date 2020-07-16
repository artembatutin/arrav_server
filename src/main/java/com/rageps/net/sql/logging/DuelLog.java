package com.rageps.net.sql.logging;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Created by Ryley Kimmel on 11/7/2016.
 */
public final class DuelLog extends DatabaseTransaction {
	private static final Gson GSON = new Gson();

	private final Player player;

	private final Player other;

	private final Position position;

	private final List<Item> staked;

	private final List<Item> received;

	private final boolean won;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public DuelLog(Player player, Player other, List<Item> staked, List<Item> received, boolean won) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.other = other;
		this.position = player.getPosition();
		this.staked = ImmutableList.copyOf(staked);
		this.received = ImmutableList.copyOf(received);
		this.won = won;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO stakes (session_id, username, ip_address, uid, other, other_session_id, won, items_staked, items_received, x, y, z, timestamp) "
			+ "VALUES (:session_id, :username, :ip_address, :uid, :other, other_session_id, :won, :items_staked, :items_received, :x, :y, :z, :timestamp);")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.getSession().getHost());
			statement.setString("uid", player.getSession().getUid());
			statement.setString("other_id", other.credentials.username);
			statement.setLong("other_session", other.getSession().getSessionId());
			statement.setBoolean("won", won);
			statement.setString("items_staked", GSON.toJson(staked));
			statement.setString("items_received", GSON.toJson(received));
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
