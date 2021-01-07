package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.TabInterface;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ForceTabPacket extends Packet {

    private final TabInterface tab;

    public ForceTabPacket(TabInterface tab) {
        this.tab = tab;
    }

    public TabInterface getTab() {
        return tab;
    }
}