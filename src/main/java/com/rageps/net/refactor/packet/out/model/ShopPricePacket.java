package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.market.MarketItem;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopPricePacket extends Packet {

    private final MarketItem item;

    public ShopPricePacket(MarketItem item) {
        this.item = item;
    }

    public MarketItem getItem() {
        return item;
    }
}