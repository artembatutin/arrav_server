package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MultiIconPacket extends Packet {

    private final boolean hide;

    public MultiIconPacket(boolean hide) {
        this.hide = hide;
    }

    public boolean isHide() {
        return hide;
    }
}