package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnInterfaceSlotPacket extends Packet {


    private final Item item;
    private final int id, slot;

    public ItemOnInterfaceSlotPacket(int id, Item item, int slot) {
        this.id = id;
        this.slot = slot;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public Item getItem() {
        return item;
    }
}