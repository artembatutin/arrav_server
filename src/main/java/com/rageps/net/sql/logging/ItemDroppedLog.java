package com.rageps.net.sql.logging;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by Ryley Kimmel on 11/3/2016.
 */
public final class ItemDroppedLog extends DatabaseTransaction {
	private final Player player;

	private final Item dropped;

	private final Position position;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public ItemDroppedLog(Player player, Item dropped, Position position) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.dropped = dropped.copy();
		this.position = position;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, "INSERT INTO dropped_items (session_id, username, ip_address, uid, item_id, amount, x, y, z, timestamp) "
				+ "VALUES (:session_id, :username, :ip_address, :uid, :item_id, :amount, :x, :y, :z, :timestamp);")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.credentials.getHostAddress());
			statement.setString("uid", player.credentials.getUid());
			statement.setInt("item_id", dropped.getId());
			statement.setInt("amount", dropped.getAmount());
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
