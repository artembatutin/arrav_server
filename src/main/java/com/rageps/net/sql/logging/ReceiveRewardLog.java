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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Created by Ryley Kimmel on 11/3/2016.
 */
public class ReceiveRewardLog extends DatabaseTransaction {

	private final Player player;

	private final Position position;

	private final List<Item> items;

	private final Type type;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	private static final Gson GSON = new Gson();


	public ReceiveRewardLog(Player player, Type type, List<Item> items) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.type = type;
		this.position = player.getPosition();
		this.items = ImmutableList.copyOf(items);
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO received_rewards (session_id, username, ip_address, uid, reward_type, items, x, y, z, timestamp) "
			+ "VALUES (:session_id, :username, :ip_address, :uid, :reward_type, :items, :x, :y, :z, :timestamp);")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.credentials.getHostAddress());
			statement.setString("uid", player.credentials.getUid());
			statement.setString("reward_type", type.name());
			statement.setString("items", GSON.toJson(items));
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}

	public enum Type {
		MYSTERY_BOX,
		CLUE_SCROLL,
		CASKET,
		RUINED_BACKPACK,
		SUPER_MYSTERY_BOX,
		EASTER_MYSTERY_BOX,
		SKILLING_SUPPLY_CRATE,
		CRYSTAL_CHEST,
		PK_REWARD,
		GIVE_ITEM,
		REDEEM_RANK_TICKET,
	}
}
