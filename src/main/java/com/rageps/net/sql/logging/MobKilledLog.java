package com.rageps.net.sql.logging;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.rageps.net.sql.DatabaseTransaction;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.entity.actor.mob.Mob;
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
public final class MobKilledLog extends DatabaseTransaction {

	private static final Gson GSON = new Gson();

	private final Player player;

	private final Position position;

	private final Mob npc;

	private final List<Item> dropped;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public MobKilledLog(Player player, Mob mob, List<Item> dropped) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.position = mob.getPosition();
		this.npc = mob;
		this.dropped = ImmutableList.copyOf(dropped);
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO npc_deaths (session_id, killer, ip_address, uid, npc_name, npc_id, items_dropped, x, y, z, timestamp) "
			+ "VALUES (:session_id, :killer, :ip_address, :uid, :npc_name, :npc_id, :items_dropped, :x, :y, :z, :timestamp);")) {
			statement.setLong("session_id", player.getSession().getSessionId());
			statement.setString("killer", player.credentials.username);
			statement.setString("ip_address", player.getSession().getHost());
			statement.setString("uid", player.getSession().getUid());
			statement.setString("npc_name", npc.getDefinition().getName());
			statement.setInt("npc_id", npc.getId());
			statement.setString("items_dropped", GSON.toJson(dropped));
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}
}
