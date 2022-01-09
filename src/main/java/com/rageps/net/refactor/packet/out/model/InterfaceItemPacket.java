package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceItemPacket extends Packet {

    private final int widget, itemId;

    public InterfaceItemPacket(int widget, int itemId) {
        this.widget = widget;
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getWidget() {
        return widget;
    }
}