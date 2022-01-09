package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.TabInterface;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TabPacket extends Packet {

    private final int id;
    private final TabInterface tab;

    public TabPacket(int id, TabInterface tab) {
        this.id = id;
        this.tab = tab;
    }

    public int getId() {
        return id;
    }

    public TabInterface getTab() {
        return tab;
    }
}