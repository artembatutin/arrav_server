package com.rageps.net.sql.logging;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Created by Ryley Kimmel on 11/4/2016.
 */
public final class PlayerDeathLog extends DatabaseTransaction {
	private static final Gson GSON = new Gson();

	private final Player player;

	private final Position position;

	private final Actor killer;

	private final List<Item> dropped;

	private final List<Item> kept;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public PlayerDeathLog(Player player, Actor killer, List<Item> dropped, List<Item> kept) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.position = player.getPosition();
		this.killer = killer;
		this.dropped = ImmutableList.copyOf(dropped);
		this.kept = ImmutableList.copyOf(kept);
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO player_deaths (session_id, username, ip_address, uid, killer, items_dropped, items_kept, x, y, z, timestamp) "
			+ "VALUES (:session_id, :username, :ip_address, :uid, :killer, :items_dropped, :items_kept, :x, :y, :z, :timestamp);")) {
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.credentials.getHostAddress());
			statement.setString("uid", player.credentials.getUid());
			statement.setString("killer", killer == null ? "null" : killer.toString());
			statement.setString("items_dropped", GSON.toJson(dropped));
			statement.setString("items_kept", GSON.toJson(kept));
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
