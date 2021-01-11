package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopAccessPacketPacket extends Packet {

    private final int shopId;

    public ShopAccessPacketPacket(int shopId) {
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }
}