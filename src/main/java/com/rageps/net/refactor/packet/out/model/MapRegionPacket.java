package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MapRegionPacket extends Packet {

    private final Position position;

    public MapRegionPacket(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}