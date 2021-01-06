package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ArrowPositionPacket extends Packet {


    private final Position position;
    private final int direction;

    /**
     * The message that sends a hint arrow on a position.
     * @param position the position to send the arrow on.
     * @param direction the direction on the position to send the arrow on. The
     * possible directions to put the arrow on are as follows:
     * <p>
     * <p>
     * Middle: 2
     * <p>
     * West: 3
     * <p>
     * East: 4
     * <p>
     * South: 5
     * <p>
     * North: 6
     * <p>
     * <p>
     */
    public ArrowPositionPacket(Position position, int direction) {
        this.position = position;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Position getPosition() {
        return position;
    }
}