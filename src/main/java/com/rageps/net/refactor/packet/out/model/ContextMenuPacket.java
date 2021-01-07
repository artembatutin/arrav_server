package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ContextMenuPacket extends Packet {

    private final int slot;
    private final boolean top;
    private final String option;

    public ContextMenuPacket(int slot, boolean top, String option) {
        this.slot = slot;
        this.top = top;
        this.option = option;
    }

    public int getSlot() {
        return slot;
    }

    public String getOption() {
        return option;
    }

    public boolean isTop() {
        return top;
    }
}