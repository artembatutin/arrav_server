package com.rageps.net.sql.logging;

import com.rageps.action.mob.Shops;
import com.rageps.content.market.MarketShop;
import com.rageps.content.market.currency.Currency;
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
 * Created by Ryley Kimmel on 11/7/2016.
 */
public final class ItemBoughtLog extends DatabaseTransaction {
	private final Player player;

	private final Position position;

	private final Item item;

	private final MarketShop shop;

	private final Currency currency;

	private final int totalCost;

	private final Timestamp timestamp = Timestamp.from(Instant.now(DateTimeUtil.CLOCK));

	public ItemBoughtLog(Player player, Item item, MarketShop shop, Currency currency, int totalCost) {
		super(TableRepresentation.LOGGING);
		this.player = player;
		this.position = player.getPosition();
		this.item = item.copy();
		this.shop = shop;
		this.currency = currency;
		this.totalCost = totalCost;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection,
		 "INSERT INTO bought_items (username, ip_address, uid, shop_id, shop_name, item_id, amount, currency, total_cost, x, y, z, timestamp) "
			+ "VALUES (:username, :ip_address, :uid, :shop_id, :shop_name, :item_id, :amount, :currency, :total_cost, :x, :y, :z, :timestamp);")) {
			statement.setString("username", player.credentials.username);
			statement.setString("ip_address", player.getSession().getHost());
			statement.setString("uid", player.getSession().getUid());
			statement.setInt("shop_id", shop.getId());
			statement.setString("shop_name", shop.getTitle());
			statement.setInt("item_id", item.getId());
			statement.setInt("amount", item.getAmount());
			statement.setString("currency", currency.getCurrency().toString());
			statement.setInt("total_cost", totalCost);
			statement.setInt("x", position.getX());
			statement.setInt("y", position.getY());
			statement.setInt("z", position.getZ());
			statement.setTimestamp("timestamp", timestamp);
			statement.executeUpdate();
			connection.commit();
		}
	}

}
