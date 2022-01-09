package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class LinkPacket extends Packet {

    private final String link;

    public LinkPacket(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}