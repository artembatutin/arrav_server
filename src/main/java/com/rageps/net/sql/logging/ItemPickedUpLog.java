package com.rageps.net.sql.logging;

import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by Ryley Kimmel on 11/4/2016.
 */
public final class ItemPickedUpLog extends DatabaseTransaction {
	private final Player player;

	private final GroundItem item;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public ItemPickedUpLog(Player player, GroundItem item) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.item = item;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO looted_items (session_id, username, ip_address, uid, dropped_by, item_id, amount, x, y, z, timestamp) "
			+ "VALUES (:session_id, :username, :ip_address, :uid, :dropped_by, :item_id, :amount, :x, :y, :z, :timestamp);")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.getSession().getHost());
			statement.setString("uid", player.getSession().getUid());
			statement.setString("dropped_by", item.getPlayer().credentials.username);
			Item dropped = item.getItem();
			statement.setInt("item_id", dropped.getId());
			statement.setInt("amount", dropped.getAmount());
			Position position = item.getPosition();
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
