package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;

/**
 * todo this should have position offset?
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class GroundItemPacket extends Packet {

    private final GroundItem item;

    public GroundItemPacket(GroundItem item) {
        this.item = item;
    }

    public GroundItem getItem() {
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroundItemPacket) {
           GroundItemPacket other = (GroundItemPacket) obj;
            return item.equals(other.item) && item.getPosition().equals(other.getItem().getPosition());
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return item.hashCode() * prime;
    }
}
