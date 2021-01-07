package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.market.MarketItem;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopStockPacket extends Packet {

    private final MarketItem item;

    public ShopStockPacket(MarketItem item) {
        this.item = item;
    }

    public MarketItem getItem() {
        return item;
    }
}