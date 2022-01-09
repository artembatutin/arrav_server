package com.rageps.tool;

import com.rageps.content.market.MarketCounter;
import com.rageps.content.market.currency.Currency;
import com.rageps.net.sql.logging.ItemBoughtLog;
import com.rageps.util.json.impl.ItemDefinitionLoader;
import com.rageps.util.json.impl.ShopLoader;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.item.Item;

import java.sql.SQLException;

public class ItemBoughtLogTest {

    public static void main(String[] args) {
        PlayerCredentials credentials = new PlayerCredentials("test", "test");
        Player p = new Player(credentials);
        new ItemDefinitionLoader().load();
        new ShopLoader().load();
        ItemBoughtLog log = new ItemBoughtLog(p, new Item(4151, 1), MarketCounter.getShops().get(0), Currency.COINS, 1500);
        try {
            log.execute(log.getRepresentation().getWrapper().open());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
