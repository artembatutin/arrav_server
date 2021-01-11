package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.market.MarketItem;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.in.model.ItemOnItemNodePacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.util.log.impl.DropItemLog;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnItemNodePacketPacketHandler implements PacketHandler<ItemOnItemNodePacketPacket> {

    @Override
    public void handle(Player player, ItemOnItemNodePacketPacket packet) {

        int itemId = packet.getItemID();
        int itemX = packet.getItemX();
        int itemY = packet.getItemY();

        if(itemY < 0 || itemId < 0 || itemX < 0)
            return;
        Position position = new Position(itemX, itemY, player.getPosition().getZ());
        Region reg = World.getRegions().getRegion(position);
        if(reg != null) {
            Optional<GroundItem> item = reg.getItem(itemId, position);
            item.ifPresent(groundItem -> player.getMovementListener().append(() -> {
                if(player.getPosition().same(new Position(itemX, itemY, player.getPosition().getZ()))) {
                    //actions
                }
            }));
        }
    }
}
