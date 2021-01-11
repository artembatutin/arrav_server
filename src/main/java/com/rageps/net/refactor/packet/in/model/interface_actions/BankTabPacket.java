package com.rageps.net.refactor.packet.in.model.interface_actions;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class BankTabPacket extends Packet {

    private final int tab, fromSlot, toTab;

    public BankTabPacket(int tab, int fromSlot, int toTab) {
        this.tab = tab;
        this.fromSlot = fromSlot;
        this.toTab = toTab;
    }

    public int getFromSlot() {
        return fromSlot;
    }

    public int getToTab() {
        return toTab;
    }

    public int getTab() {
        return tab;
    }
}