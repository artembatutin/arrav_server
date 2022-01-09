package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CoordinatesPacket extends Packet {


    private final Position position;

    private final Position lastRegion;

    public CoordinatesPacket(Position position, Position lastRegion) {
        this.position = position;
        this.lastRegion = lastRegion;
    }

    public Position getPosition() {
        return position;
    }

    public Position getLastRegion() {
        return lastRegion;
    }
}