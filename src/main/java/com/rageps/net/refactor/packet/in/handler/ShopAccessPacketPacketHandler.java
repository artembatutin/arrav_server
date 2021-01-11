package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.market.MarketCounter;
import com.rageps.net.refactor.packet.in.model.ShopAccessPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopAccessPacketPacketHandler implements PacketHandler<ShopAccessPacketPacket> {

    @Override
    public void handle(Player player, ShopAccessPacketPacket packet) {

        int shop = packet.getShopId();
        //todo verficiation
        if(MarketCounter.getShops().containsKey(shop)) {
            MarketCounter.getShops().get(shop).openShop(player);
        }
    }
}
