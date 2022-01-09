package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopPacket extends Packet {

    private final int id;
    private final IntArrayList items;

    public ShopPacket(int id, IntArrayList items) {
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public IntArrayList getItems() {
        return items;
    }
}