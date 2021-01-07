package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemNodePacket extends Packet {

    private final GroundItem item;
    private final Player player;

    public ItemNodePacket(GroundItem item, Player player) {
        this.item = item;
        this.player = player;
    }

    public GroundItem getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }
}